package com.enquero.driverfactory.web_selenium.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Queue;

public class ImageUtil {

    public static void binarize(BufferedImage srcImg, double maxLuminance) {
        int rgb, red, green, blue;
        int blackRgb = Color.BLACK.getRGB();
        int whiteRgb = Color.WHITE.getRGB();

        for (int x = 0; x < srcImg.getWidth(); ++x) {
            for (int y = 0; y < srcImg.getHeight(); ++y) {
                rgb = srcImg.getRGB(x, y);
                red = (rgb >> 16) & 0xFF;
                green = (rgb >> 8) & 0xFF;
                blue = rgb & 0xFF;
                double luminance = 0.2126 * red + 0.7152 * green + 0.0722 * blue;
                if (luminance >= maxLuminance) {
                    srcImg.setRGB(x, y, whiteRgb);
                } else {
                    srcImg.setRGB(x, y, blackRgb);
                }
            }
        }
    }

    public static BufferedImage captureImage(Rectangle captureRect) {
        try {
            return new Robot().createScreenCapture(captureRect);
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Failed to capture image in rectangle (%s, %s, %s, %s)",
                    captureRect.x,
                    captureRect.y,
                    captureRect.width,
                    captureRect.height), ex);
        }
    }

    public static BufferedImage cloneImageRegion(BufferedImage srcImg, int x, int y, int width, int height) {
        int imgType = (srcImg.getTransparency() == Transparency.OPAQUE)
                ? BufferedImage.TYPE_INT_RGB
                : BufferedImage.TYPE_INT_ARGB;
        BufferedImage newImage = new BufferedImage(width, height, imgType);
        Graphics2D g2 = newImage.createGraphics();
        g2.drawImage(srcImg.getSubimage(x, y, width, height), 0, 0, null);
        g2.dispose();

        return newImage;
    }

    /**
     * Compare two images and return an image that shows the differences and the
     * similarity between them, as a percent.
     */
    public static ImageCompareResult compare(BufferedImage templateImage, BufferedImage imageToCompare, Double maxPercentColorDistance, Color ignoredPixelsColor) {
        try {
            if (maxPercentColorDistance == null) {
                maxPercentColorDistance = 0.15;
            }

            BufferedImage diffImage = new BufferedImage(
                    Math.min(templateImage.getWidth(), imageToCompare.getWidth()),
                    Math.min(templateImage.getHeight(), imageToCompare.getHeight()),
                    BufferedImage.TYPE_3BYTE_BGR);

            double maxColorDistance = Math.sqrt(
                    Math.pow(255, 2)
                    + Math.pow(255, 2)
                    + Math.pow(255, 2));

            long diffPixelCount = 0;
            int red = Color.RED.getRGB();
            int gray = Color.LIGHT_GRAY.getRGB();
            int reallyLightGray = Color.decode("#FAFAFA").getRGB();

            for (int x = 0; x < diffImage.getWidth(); x++) {
                for (int y = 0; y < diffImage.getHeight(); y++) {
                    int color1 = templateImage.getRGB(x, y);
                    int red1 = (color1 >> 16) & 0x000000FF;
                    int green1 = (color1 >> 8) & 0x000000FF;
                    int blue1 = (color1) & 0x000000FF;
     
                    int color2 = imageToCompare.getRGB(x, y);
                    int red2 = (color2 >> 16) & 0x000000FF;
                    int green2 = (color2 >> 8) & 0x000000FF;
                    int blue2 = (color2) & 0x000000FF;
                    
                    if (ignoredPixelsColor != null) {
                        int ignoredColor = ignoredPixelsColor.getRGB();
                        if (color1 == ignoredColor || color2 == ignoredColor) {
                            diffImage.setRGB(x, y, reallyLightGray);
                            continue;
                        }
                    }

                    double colorDistance = Math.sqrt(
                            Math.pow(red1 - red2, 2)
                            + Math.pow(green1 - green2, 2)
                            + Math.pow(blue1 - blue2, 2));
                    double percentDistance = colorDistance / maxColorDistance;

                    // Make new color of the pixel either red or light gray,
                    // depending on how different the actual screen capture was
                    // from the reference image.
                    if (percentDistance >= maxPercentColorDistance) {
                        diffPixelCount++;
                        diffImage.setRGB(x, y, red);
                    } else if (percentDistance > 0.01) {
                        diffImage.setRGB(x, y, gray);
                    } else {
                        diffImage.setRGB(x, y, reallyLightGray);
                    }
                }
            }

            double similarity = 1 - ((double) diffPixelCount / (diffImage.getWidth() * diffImage.getHeight()));
            return new ImageCompareResult(diffImage, similarity, diffPixelCount);
        } catch (Exception exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     * Returns a number between 0 and 255 that represents the distance in 3D
     * space between the RGB values of the two colors.
     */
    public static double distanceBetweenColors(Color color1, Color color2) {
        return Math.sqrt(
                Math.pow(color1.getRed() - color2.getRed(), 2)
                + Math.pow(color1.getGreen() - color2.getGreen(), 2)
                + Math.pow(color1.getBlue() - color2.getBlue(), 2));
    }

    /**
     * Returns a number between 0 and 255 that represents the distance in 3D
     * space between the RGB values of the two colors.
     */
    public static double distanceBetweenColors(int rgb1, int rgb2) {
        int red1 = (rgb1 >> 16) & 0xFF;
        int green1 = (rgb1 >> 8) & 0xFF;
        int blue1 = rgb1 & 0xFF;

        int red2 = (rgb2 >> 16) & 0xFF;
        int green2 = (rgb2 >> 8) & 0xFF;
        int blue2 = rgb2 & 0xFF;

        return Math.sqrt(
                Math.pow(red1 - red2, 2)
                + Math.pow(green1 - green2, 2)
                + Math.pow(blue1 - blue2, 2));
    }

    public static void floodFillImage(BufferedImage image, int x, int y, Color color, double tolerancePercent) {
        int tolerance = (int) (255 * tolerancePercent);
        int srcColor = image.getRGB(x, y);
        boolean[][] hits = new boolean[image.getHeight()][image.getWidth()];

        Queue<Point> queue = new LinkedList<Point>();
        queue.add(new Point(x, y));

        while (!queue.isEmpty()) {
            Point p = queue.remove();

            if (floodFillImageWorker(image, hits, p.x, p.y, srcColor, color.getRGB(), tolerance)) {
                queue.add(new Point(p.x, p.y - 1));
                queue.add(new Point(p.x, p.y + 1));
                queue.add(new Point(p.x - 1, p.y));
                queue.add(new Point(p.x + 1, p.y));
            }
        }
    }

    private static boolean floodFillImageWorker(BufferedImage image, boolean[][] hits, int x, int y, int srcColor, int targetColor, int tolerance) {
        if (y < 0) {
            return false;
        }
        if (x < 0) {
            return false;
        }
        if (y > image.getHeight() - 1) {
            return false;
        }
        if (x > image.getWidth() - 1) {
            return false;
        }

        if (hits[y][x]) {
            return false;
        }

        if (distanceBetweenColors(image.getRGB(x, y), srcColor) > tolerance) {
            return false;
        }

        // valid, paint it
        image.setRGB(x, y, targetColor);
        hits[y][x] = true;
        return true;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     *
     * @param srcImg - Source image to scale
     * @param width - Desired width
     * @param height - Desired height
     * @return - The new resized image
     */
    public static BufferedImage scaleImage(BufferedImage srcImg, int width, int height) {
        BufferedImage resizedImg = new BufferedImage(width, height, srcImg.getType());
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2.drawImage(srcImg, 0, 0, width, height, null);
        g2.dispose();

        return resizedImg;
    }

    /**
     * Resizes an image using a Graphics2D object backed by a BufferedImage.
     *
     * @param srcImg - Source image to scale
     * @param width - Desired width
     * @param height - Desired height
     * @return - The new resized image
     */
    public static BufferedImage scaleImage(BufferedImage srcImg, double scalingPercent) {
        return scaleImage(
                srcImg,
                (int) (scalingPercent * srcImg.getWidth()),
                (int) (scalingPercent * srcImg.getHeight()));
    }
}
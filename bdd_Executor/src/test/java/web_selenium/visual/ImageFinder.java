package web_selenium.visual;

import nu.pattern.OpenCV;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import web_selenium.contracts.IImageFinder;
import web_selenium.exceptions.ImageNotFoundException;
import web_selenium.logging.Logger;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class ImageFinder implements IImageFinder {

    static {
        OpenCV.loadShared();
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);
    }

    MatchingMethod matchingMethod;

    public ImageFinder() {
        this.matchingMethod = MatchingMethod.MM_SQUARE_DIFFERENCE;
    }

    /**
     * Finds any one of the template images in a source image. The method
     * iterates through the template images, performs a find operation for each
     * one of them and selects the one that produced the best accuracy. Throws
     * an exception when no image was found or the desired accuracy couldn't be
     * met.
     *
     * @param sourceImage The source image.
     * @param sourceRect The rectangle in the source image to look into. If
     * null, the find operation will look into the whole source image.
     * @param templateImages The collection of template images to look for in
     * the source image.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    @Override
    public ImageFinderResult findAnyImage(BufferedImage sourceImage, Rectangle sourceRect, List<BufferedImage> templateImages, double desiredAccuracy) {
        ImageFinderResult bestResult = new ImageFinderResult(new Rectangle(100, 100, 100, 100), 0);

        for (BufferedImage templateImage : templateImages) {
            try {
                ImageFinderResult result = this.findImage(sourceImage, sourceRect, templateImage, 0);
                if (result.getAccuracy() > bestResult.getAccuracy()) {
                    bestResult = result;
                }
            } catch (UnsupportedOperationException ex) {
                // This exception will be thrown when the template
                // image is larger than the source image
            } catch (Exception ex) {
                Logger.warning(
                        "Failed to perform an image template matching operation",
                        ex);
            }
        }

        if (bestResult.getAccuracy() < desiredAccuracy) {
            String messagePrefix = templateImages.size() == 1
                    ? "Failed to find the template image"
                    : String.format("Failed to find one of %s template images", templateImages.size());

            throw new ImageNotFoundException(
                    String.format(
                            "%s in the source image at (%s, %s, %s, %s). The best accuracy was %.2f and the desired accuracy was %.2f",
                            messagePrefix,
                            sourceRect.x,
                            sourceRect.y,
                            sourceRect.width,
                            sourceRect.height,
                            bestResult.getAccuracy(),
                            desiredAccuracy), bestResult.getFoundRect(), bestResult.getAccuracy());
        }

        return bestResult;
    }

    /**
     * Finds a template image on the screen. Throws an exception when the image
     * wasn't found or the desired accuracy couldn't be met.
     *
     * @param sourceScreenRect The rectangle on the screen to look into.
     * @param templateImage The template image to find.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    @Override
    public ImageFinderResult findImage(Rectangle sourceScreenRect, BufferedImage templateImage, double desiredAccuracy) {
        try {
            BufferedImage capture = new Robot().createScreenCapture(sourceScreenRect);
            Mat sourceMat = CvHelper.convertToMat(capture);
            Mat templateMat = CvHelper.convertToMat(templateImage);
            return this.findImage(sourceMat, templateMat, desiredAccuracy);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "An error ocurred while trying to find an image on screen at (%s, %s, %s, %s)",
                    sourceScreenRect.x,
                    sourceScreenRect.y,
                    sourceScreenRect.width,
                    sourceScreenRect.height), ex);
        }
    }

    /**
     * Finds a template image on the screen. Throws an exception when the image
     * wasn't found or the desired accuracy couldn't be met.
     *
     * @param sourceScreenRect The rectangle on the screen to look into.
     * @param templateImage The template image to find.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    @Override
    public ImageFinderResult findImage(Rectangle sourceScreenRect, File templateImage, double desiredAccuracy) {
        try {
            BufferedImage capture = new Robot().createScreenCapture(sourceScreenRect);
            Mat sourceMat = CvHelper.convertToMat(capture);
            Mat templateMat = Imgcodecs.imread(templateImage.getAbsolutePath());
            return this.findImage(sourceMat, templateMat, desiredAccuracy);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "An error ocurred while trying to find an image on screen at (%s, %s, %s, %s)",
                    sourceScreenRect.x,
                    sourceScreenRect.y,
                    sourceScreenRect.width,
                    sourceScreenRect.height), ex);
        }
    }

    /**
     * Finds a template image in a source image. Throws an exception when the
     * image wasn't found or the desired accuracy couldn't be met.
     *
     * @param sourceMat The source image.
     * @param templateMat The template image to find in the source image.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    private ImageFinderResult findImage(Mat sourceMat, Mat templateMat, double desiredAccuracy) {
        if (sourceMat.width() < templateMat.width() || sourceMat.height() < templateMat.height()) {
            throw new UnsupportedOperationException("The template image is larger than the source image. Ensure that the width and/or height of the image you are trying to find do not exceed the dimensions of the source image.");
        }

        Mat result = new Mat(sourceMat.rows() - templateMat.rows() + 1, sourceMat.rows() - templateMat.rows() + 1, CvType.CV_32FC1);
        int intMatchingMethod;

        switch (this.matchingMethod) {
            case MM_CORELLATION_COEFF:
                intMatchingMethod = Imgproc.TM_CCOEFF_NORMED;
                break;
            case MM_CROSS_CORELLATION:
                intMatchingMethod = Imgproc.TM_CCORR_NORMED;
                break;
            default:
                intMatchingMethod = Imgproc.TM_SQDIFF_NORMED;
        }

        Imgproc.matchTemplate(sourceMat, templateMat, result, intMatchingMethod);
        MinMaxLocResult minMaxLocRes = Core.minMaxLoc(result);

        double accuracy = 0;
        Point location = null;

        if (this.matchingMethod == MatchingMethod.MM_SQUARE_DIFFERENCE) {
            accuracy = 1 - minMaxLocRes.minVal;
            location = minMaxLocRes.minLoc;
        } else {
            accuracy = minMaxLocRes.maxVal;
            location = minMaxLocRes.maxLoc;
        }

        if (accuracy < desiredAccuracy) {
            throw new ImageNotFoundException(
                    String.format(
                            "Failed to find template image in the source image. The accuracy was %.2f and the desired accuracy was %.2f",
                            accuracy,
                            desiredAccuracy),
                    new Rectangle((int) location.x, (int) location.y, templateMat.width(), templateMat.height()),
                    accuracy);
        }

        if (!minMaxLocResultIsValid(minMaxLocRes)) {
            throw new ImageNotFoundException(
                    "Image find result (MinMaxLocResult) was invalid. This usually happens when the source image is covered in one solid color.",
                    null,
                    null);
        }

        Rectangle foundRect = new Rectangle(
                (int) location.x,
                (int) location.y,
                templateMat.width(),
                templateMat.height());

        return new ImageFinderResult(foundRect, accuracy);
    }

    /**
     * Finds a template image in a source image. Throws an exception when the
     * image wasn't found or the desired accuracy couldn't be met.
     *
     * @param sourceImage The source image.
     * @param templateImage The template image to find in the source image.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    @Override
    public ImageFinderResult findImage(File sourceImage, File templateImage, double desiredAccuracy) {
        Mat sourceMat = Imgcodecs.imread(sourceImage.getAbsolutePath());
        Mat templateMat = Imgcodecs.imread(templateImage.getAbsolutePath());
        return this.findImage(sourceMat, templateMat, desiredAccuracy);
    }

    /**
     * Finds a template image in a source image. Throws an exception when the
     * image wasn't found or the desired accuracy couldn't be met.
     *
     * @param sourceImage The source image.
     * @param sourceRect The rectangle in the source image to look into. If
     * null, the find operation will look into the whole source image.
     * @param templateImage The template image to find in the source image.
     * @param desiredAccuracy The desired accuracy of the find operation as a
     * number between 0 and 1.
     * @return An ImageFinderResult object that stores the rectangle of the
     * found image and desired accuracy.
     */
    @Override
    public ImageFinderResult findImage(BufferedImage sourceImage, Rectangle sourceRect, BufferedImage templateImage, double desiredAccuracy) {
        BufferedImage subImage = sourceImage;
        if (sourceRect != null) {
            subImage = sourceImage.getSubimage(
                    sourceRect.x,
                    sourceRect.y,
                    sourceRect.width,
                    sourceRect.height);
        }

        Mat sourceMat = CvHelper.convertToMat(subImage);
        Mat templateMat = CvHelper.convertToMat(templateImage);

        return this.findImage(sourceMat, templateMat, desiredAccuracy);
    }

    /**
     * Returns the matching method that is being used by this ImageFinder
     * instance (MM_CORELLATION_COEFF, MM_CROSS_CORELLATION or
     * MM_SQUARE_DIFFERENCE).
     */
    public MatchingMethod getMatchingMethod() {
        return matchingMethod;
    }

    /**
     * Checks whether an OpenCV MinMaxLocResult object is valid. This object is
     * used for storing the location of the minimum and maximum values for an
     * image find operation, along with the actual values themselves.
     */
    private boolean minMaxLocResultIsValid(MinMaxLocResult minMaxLocRes) {
        if (minMaxLocRes.minVal == 1
                && minMaxLocRes.maxVal == 1
                && minMaxLocRes.maxLoc.x == 0
                && minMaxLocRes.maxLoc.y == 0
                && minMaxLocRes.minLoc.x == 0
                && minMaxLocRes.minLoc.y == 0) {

            return false;
        } else {
            return true;
        }
    }

    /**
     * Sets the matching method that will be used by this ImageFinder instance
     * for future find operations (MM_CORELLATION_COEFF, MM_CROSS_CORELLATION or
     * MM_SQUARE_DIFFERENCE).
     */
    public void setMatchingMethod(MatchingMethod matchingMethod) {
        this.matchingMethod = matchingMethod;
    }
}

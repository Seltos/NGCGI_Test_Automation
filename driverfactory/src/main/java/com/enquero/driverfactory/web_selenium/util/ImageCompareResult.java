package com.enquero.driverfactory.web_selenium.util;

import java.awt.image.BufferedImage;

/**
 * Stores the result of an image compare operation.
 */
public class ImageCompareResult {

    private BufferedImage diffImage;
    
    private long diffPixelCount = 0;

    private double similarity;

    public ImageCompareResult(BufferedImage diffImage, double similarity, long diffPixelCount) {
        this.diffImage = diffImage;
        this.similarity = similarity;
        this.diffPixelCount = diffPixelCount;
    }

    /**
     * Returns the similarity between two images as a percent.
     */
    public double getSimilarity() {
        return similarity;
    }

    /**
     * Returns an image highlighting the differences between the two source
     * images.
     */
    public BufferedImage getDiffImage() {
        return diffImage;
    }
    
    /**
     * Returns the number of pixels that were different between the two images.
     */
    public long getDiffPixelCount() {
        return diffPixelCount;
    }
}

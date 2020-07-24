package web_selenium.visual;

import java.awt.*;

/**
 * Stores the found rectangle and the accuracy of an image find operation.
 */
public class ImageFinderResult {

    private double accuracy;

    private Rectangle foundRect;

    public ImageFinderResult(Rectangle foundRect, double accuracy) {
        this.foundRect = foundRect;
        this.accuracy = accuracy;
    }

    /**
     * Returns the accuracy of the image find operation, as a number between 0
     * and 1.
     */
    public double getAccuracy() {
        return accuracy;
    }

    /**
     * Returns the rectangle where a template image was found in a source image.
     */
    public Rectangle getFoundRect() {
        return foundRect;
    }
}

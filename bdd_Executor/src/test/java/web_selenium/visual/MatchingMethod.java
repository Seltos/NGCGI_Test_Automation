package web_selenium.visual;

/**
 * List of template matching methods for the OpenCV algorithm. We are always
 * using the "_NORMED" version of the methods.
 */
public enum MatchingMethod {
    /**
     * Normalized correlation coefficient.
     */
    MM_CORELLATION_COEFF,
    
    /**
     * Normalized cross correlation.
     */
    MM_CROSS_CORELLATION,
    
    /**
     * Sum of squared differences between pixel values.
     */
    MM_SQUARE_DIFFERENCE
}

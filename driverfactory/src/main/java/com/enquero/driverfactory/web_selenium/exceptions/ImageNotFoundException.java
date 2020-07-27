package com.enquero.driverfactory.web_selenium.exceptions;

import java.awt.*;

/**
 * Thrown to indicate an image find operation failed.
 */
public class ImageNotFoundException extends RuntimeException {
    Double accuracy;
            
    public Rectangle foundRect;
    
    public ImageNotFoundException(String message, Rectangle foundRect, Double accuracy) {
        super(message);
        
        this.accuracy = accuracy;
        this.foundRect = foundRect;
    }
}

package com.enquero.driverfactory.web_selenium.contracts;


import com.enquero.driverfactory.web_selenium.visual.ImageFinderResult;
import com.enquero.driverfactory.web_selenium.visual.MatchingMethod;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public interface IImageFinder {

    ImageFinderResult findImage(Rectangle sourceScreenRect, BufferedImage templateImage, double desiredAccuracy);

    /**
     * Find one of multiple template images in the source image.
     */
    ImageFinderResult findAnyImage(BufferedImage sourceImage, Rectangle sourceRect, List<BufferedImage> templateImages, double desiredAccuracy);
    
    ImageFinderResult findImage(BufferedImage sourceImage, Rectangle sourceRect, BufferedImage templateImage, double desiredAccuracy);

    ImageFinderResult findImage(File sourceImage, File templateImage, double desiredAccuracy);

    ImageFinderResult findImage(Rectangle sourceScreenRect, File templateImage, double desiredAccuracy);

    public MatchingMethod getMatchingMethod();

    public void setMatchingMethod(MatchingMethod matchingMethod);
}

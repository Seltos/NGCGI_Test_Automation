package com.enquero.driverfactory.selenium_web_util;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@Slf4j
public class SeleniumHelper {

    public static void discardDriver(WebDriver driver) {
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ex) {
                log.warn("Failed to quit the Selenium driver",ex);
            }
        }
    }
}

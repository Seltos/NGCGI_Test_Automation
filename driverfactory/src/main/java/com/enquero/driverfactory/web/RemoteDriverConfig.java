package com.enquero.driverfactory.web;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteDriverConfig {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    WebDriver remoteWebDriver;

    public WebDriver getRemoteDriver() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities().chrome();
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        remoteWebDriver = new RemoteWebDriver(new URL("http://qa43-docker.medlife.com:4446/wd/hub"), capabilities);
        return remoteWebDriver;
    }
}

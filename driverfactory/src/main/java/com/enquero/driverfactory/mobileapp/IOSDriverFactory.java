package com.enquero.driverfactory.mobileapp;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IOSDriverFactory {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    IOSDriver<IOSElement> iosDriver;

    /*
    For Local Simulator
     */
    public IOSDriver getIOSDriver(String apkFilePath, String platformVersion) throws MalformedURLException {
        File app = new File(apkFilePath);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability(MobileCapabilityType.APPIUM_VERSION, "1.3.4");
        capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, platformVersion);
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
        capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone 7");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        iosDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        iosDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        return iosDriver;
    }

    public IOSDriver getIOSDriverForDocker(String apkFilePath,String platformVersion, String remoteHost) throws MalformedURLException {
        File app = new File(apkFilePath);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","iPhone 7");
        capabilities.setCapability("platformName","iOS");
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("automationName", "XCUITest");
        capabilities.setCapability("app", app.getAbsolutePath());
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        iosDriver = new IOSDriver(new URL("http://"+ remoteHost +":4723/wd/hub"), capabilities);
        return iosDriver;
    }

    /*
        for Real Device Native or Hybrid Apps
         */
    public IOSDriver getRealIOSDriver(String udid, String appPackage, String appActivity,String platformVersion) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName","iOS");
        capabilities.setCapability("deviceName","iPhone 7");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity",appActivity);
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("udid", udid);
        capabilities.setCapability("startIWDP", true);
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        iosDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return iosDriver;
    }


    /*
    for Real Device Web Apps
     */
    public IOSDriver getRealIOSDriverApp(String udid, String browserName,String platformVersion) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","iPhone 7");
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("udid", udid);
        capabilities.setCapability("startIWDP", true);
        capabilities.setCapability("platformName","iOS");
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        iosDriver = new IOSDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return iosDriver;
    }


}

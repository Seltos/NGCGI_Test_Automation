package com.enquero.driverfactory.mobileapp;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AndroidDriverFactory {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    AndroidDriver<AndroidElement> androidDriver;

    public AndroidDriver getAndroidDriverForDocker(String platformVersion, String remoteHost) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","emulator-5554");
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", "/tmp/app-release-1.0.0.apk");
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        androidDriver = new AndroidDriver(new URL("http://"+ remoteHost +":4723/wd/hub"), capabilities);
        return androidDriver;
    }

    /*
    For Local Emulator
     */
    public AndroidDriver getAndroidDriver(String apkFilePath) throws MalformedURLException {
        File app = new File(apkFilePath);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","emulator-5554");
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion","9");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", app.getAbsolutePath());
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        androidDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return androidDriver;
    }
}

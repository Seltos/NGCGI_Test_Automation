package com.enquero.driverfactory.mobileapp;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AndroidDriverFactory {

    public Logger logger = Logger.getLogger(this.getClass().getSimpleName());
    AndroidDriver<AndroidElement> androidDriver;
    AppiumDriverLocalService  service;
    WindowsDriver<WindowsElement> winDriver;

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
    public AndroidDriver getAndroidDriver(String apkFilePath,int platformVersion) throws MalformedURLException {
        File app = new File(apkFilePath);
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName","emulator-5554");
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("app", app.getAbsolutePath());
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        androidDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return androidDriver;
    }

    /*
    for Real Device Native or Hybrid Apps
     */
    public WebDriver getRealAndroidDriver(String deviceName, String appPackage,String appActivity, String platformVersion) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","Android");
        capabilities.setCapability("deviceName",deviceName);
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability("appActivity",appActivity);
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        androidDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return androidDriver;
    }


    /*
    for Real Device Web Apps
     */
    public WebDriver getRealAndroidDriverApp(String deviceName, String browserName, String platformVersion) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device","Android");
        capabilities.setCapability("deviceName",deviceName);
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("browserName", browserName);
        capabilities.setCapability("newCommandTimeout",60);
        logger.log(Level.INFO,Thread.currentThread().getName() + capabilities);
        androidDriver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
        return androidDriver;
    }

    public void startServer(int port, String driverExePath, String appiumJS){
        String osName = System.getProperty("os.name");
        if (osName.contains("Mac")) {
            HashMap<String, String> environment = new HashMap();
            environment.put("PATH", "/usr/local/bin:" + System.getenv("PATH"));
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(driverExePath))
                    .withAppiumJS(new File(appiumJS))
                    .withIPAddress("127.0.0.1")
                    .usingPort(port)
                    .withEnvironment(environment)
                    .withLogFile(new File("AndroidTest.log")));
        }
        else if (osName.contains("Windows")) {
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingPort(port)
                    .withLogFile(new File("IOSTest.log")));
        }
        else {
            Assert.fail("Unspecified OS found, Appium can't run");
        }
        System.out.println("- - - - - - - - Starting Appium Server- - - - - - - - ");
        if (!checkIfServerIsRunnning(port)) {
            service.start();
        }
    }

    public void stopServer(){
        service.stop();
    }

    public boolean checkIfServerIsRunnning(int port) {
        boolean isServerRunning = false;
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.close();
        } catch (IOException e) {
            isServerRunning = true;
        } finally {
            serverSocket = null;
        }
        return isServerRunning;
    }

    public WindowsDriver getWinAppDriver(String apkFilePath) throws MalformedURLException {
        DesiredCapabilities Capabilities = new DesiredCapabilities();
        Capabilities.setCapability ("app", apkFilePath);
        Capabilities.setCapability ("deviceName", "WindowsPC");
        winDriver  = new WindowsDriver(new URL("http://127.0.0.1:4723/") ,Capabilities);
        winDriver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        return winDriver;
    }

}

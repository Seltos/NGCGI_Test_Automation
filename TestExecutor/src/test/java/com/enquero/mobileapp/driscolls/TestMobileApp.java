package com.enquero.mobileapp.driscolls;

import com.enquero.driverfactory.mobileapp.AndroidDriverFactory;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

public class TestMobileApp {

    AndroidDriver<AndroidElement> androidDriver;
    @Parameters("apkFilePath")
    @BeforeClass
    public void setup(@Optional("C:\\Users\\VivekVerma\\Downloads\\app-debug.apk")String appLocation) throws MalformedURLException {
         new AndroidDriverFactory().getAndroidDriver(appLocation);
        //androidDriver.quit();
    }
    //@Test
    public void testapp()
    {
        androidDriver.getBatteryInfo();
    }
}
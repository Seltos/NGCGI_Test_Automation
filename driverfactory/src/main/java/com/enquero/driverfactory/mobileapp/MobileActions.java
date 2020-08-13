package com.enquero.driverfactory.mobileapp;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

public class MobileActions
{

    private Object String;

    public void set_appium_version(String version)
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("browserstack.appium_version", version);
    }

    public void set_visuallogs()
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("browserstack.debug", "true");
    }

    public void set_appiumlogs()
   {
      DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
      desiredCapabilities.setCapability("browserstack.appiumLogs", "false");
   }


  public void google_account_login(String googlemailid,String googlepassword)
  {
      DesiredCapabilities desiredCapabilities = new DesiredCapabilities();


      desiredCapabilities.setCapability("browserstack.appStoreConfiguration",   new HashMap<String, String>().put("username", "googlemailid"));
      desiredCapabilities.setCapability("browserstack.appStoreConfiguration",   new HashMap<String, String>().put("password", "googlepassword"));
  }

  public void upload_media(String[] media)
  {
    DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
    desiredCapabilities.setCapability("browserstack.uploadMedia", media);

  }

  //identifying UI

    public void testFindElementsById (String apk,int version) throws MalformedURLException
    {
        // Look for element by ID. In Android this is the "resource-id"

        List<WebElement> actionBarContainerElements = (List<WebElement>) new AndroidDriverFactory().getAndroidDriver(apk,version).findElementById("android:id/action_bar_container");
        Assert.assertEquals(actionBarContainerElements.size(), 1);
    }




























}

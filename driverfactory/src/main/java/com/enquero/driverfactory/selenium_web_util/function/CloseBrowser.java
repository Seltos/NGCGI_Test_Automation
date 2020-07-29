package com.enquero.driverfactory.selenium_web_util.function;


import com.enquero.driverfactory.selenium_web_util.SeleniumHelper;
import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;
import org.openqa.selenium.WebDriver;

public class CloseBrowser extends SeleniumTestAction {

    public void run(WebDriver driver) {

        SeleniumHelper.discardDriver(driver);
    }
}

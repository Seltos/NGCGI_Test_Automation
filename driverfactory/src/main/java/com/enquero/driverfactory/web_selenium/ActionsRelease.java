package com.enquero.driverfactory.web_selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ActionsRelease extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By locator = this.readLocatorArgument("locator", null);
        
        if (locator != null) {
            WebElement element = this.getElement(locator);
            SeleniumTestAction.getActionsInstance().release(element);
        } else {
            SeleniumTestAction.getActionsInstance().release();
        }
    }
}

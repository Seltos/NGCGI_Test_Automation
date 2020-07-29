package com.enquero.driverfactory.web_selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ActionsMoveTo extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By locator = this.readLocatorArgument("locator");
        Integer xOffset = this.readIntArgument("xOffset", null);
        Integer yOffset = this.readIntArgument("yOffset", null);

        WebElement element = this.getElement(locator);

        if (xOffset != null && yOffset != null) {
            SeleniumTestAction.getActionsInstance().moveToElement(element, xOffset, yOffset);
        } else {
            SeleniumTestAction.getActionsInstance().moveToElement(element);
        }
    }
}

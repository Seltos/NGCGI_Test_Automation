package com.enquero.driverfactory.web_selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class ActionsKeyUp extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By locator = this.readLocatorArgument("locator", null);
        String keyValue = this.readStringArgument("key", null);
        String charValue = this.readStringArgument("char", null);

        CharSequence key = null;
        if (keyValue != null) {
            key = Keys.valueOf(keyValue);
        } else if (charValue != null) {
            key = charValue;
        } else {
            throw new RuntimeException(
                    "You must either provide the \"key\" argument or the \"char\" "
                    + "argument to specify the key you want to press.");
        }

        if (locator != null) {
            WebElement element = this.getElement(locator);
            SeleniumTestAction.getActionsInstance().keyUp(element, key);
        } else {
            SeleniumTestAction.getActionsInstance().keyUp(key);
        }
    }
}

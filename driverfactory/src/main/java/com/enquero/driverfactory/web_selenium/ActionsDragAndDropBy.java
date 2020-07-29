package com.enquero.driverfactory.web_selenium;


import com.enquero.driverfactory.web_selenium.annotations.TestActionArgument;
import com.enquero.driverfactory.web_selenium.annotations.TestActionClass;
import com.enquero.driverfactory.web_selenium.annotations.Type;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@TestActionClass(description = "Clicks an UI element or the current mouse location.")
@TestActionArgument(
        name = "locator", type = Type.OBJECT, optional = true,
        description = "Locator object that identifies the element to click on. If "
        + "this argument is ommitted, we will click at the current mouse location.")

public class ActionsDragAndDropBy extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By locator = this.readLocatorArgument("locator");
        Integer xOffset = this.readIntArgument("xOffset", 0);
        Integer yOffset = this.readIntArgument("yOffset", 0);

        WebElement element = this.getElement(locator);
        
        SeleniumTestAction.getActionsInstance().dragAndDropBy(element, xOffset, yOffset);
    }
}
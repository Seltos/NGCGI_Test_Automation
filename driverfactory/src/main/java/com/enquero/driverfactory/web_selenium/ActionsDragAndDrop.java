package com.enquero.driverfactory.web_selenium;

import com.enquero.driverfactory.web_selenium.annotations.TestActionArgument;
import com.enquero.driverfactory.web_selenium.annotations.TestActionClass;
import com.enquero.driverfactory.web_selenium.annotations.Type;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@TestActionClass(
        description = "Performs click-and-hold at the location of the source element, "
        + "moves to the location of the target element, then releases the mouse.")
@TestActionArgument(
        name = "locator", type = Type.OBJECT, optional = true,
        description = "Locator object that identifies the element to click on. If "
        + "this argument is ommitted, we will click at the current mouse location.")

public class ActionsDragAndDrop extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By sourceLocator = this.readLocatorArgument("source");
        By targetLocator = this.readLocatorArgument("target");

        WebElement sourceElement = this.getElement(sourceLocator);
        WebElement targetElement = this.getElement(targetLocator);

        SeleniumTestAction.getActionsInstance().dragAndDrop(sourceElement, targetElement);
    }
}

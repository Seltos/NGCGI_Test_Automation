package com.enquero.driverfactory.web_selenium;


import com.enquero.driverfactory.web_selenium.annotations.TestActionArgument;
import com.enquero.driverfactory.web_selenium.annotations.TestActionClass;
import com.enquero.driverfactory.web_selenium.annotations.Type;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

@TestActionClass(
        description = "Performs a double-click at the current mouse location or "
        + "at middle of the specified element.")
@TestActionArgument(
        name = "locator", type =  Type.OBJECT, optional = true,
        description = "Locator object that identifies the element to double-click on. If "
        + "this argument is ommitted, we will double-click at the current mouse location.")

public class ActionsDoubleClick extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        By locator = this.readLocatorArgument("locator", null);

        if (locator != null) {
            WebElement element = this.getElement(locator);
            SeleniumTestAction.getActionsInstance().doubleClick(element);
        } else {
            SeleniumTestAction.getActionsInstance().doubleClick();
        }
    }
}

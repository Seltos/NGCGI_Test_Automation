package com.enquero.driverfactory.web_selenium;


import com.enquero.driverfactory.web_selenium.annotations.TestActionArgument;
import com.enquero.driverfactory.web_selenium.annotations.TestActionClass;
import com.enquero.driverfactory.web_selenium.annotations.Type;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

@TestActionClass(description = "Simulates pressing a modifier key. Does not release "
        + "the modifier key - subsequent interactions may assume it's kept pressed. "
        + "Note that the modifier key is never released implicitly - either ActionsKeyUp "
        + "or ActionsSendKeys(with key=NULL) must be called to release the modifier.")
@TestActionArgument(
        name = "locator", type = Type.OBJECT, optional = true,
        description = "Indicates the UI element to focus on before doing the "
        + "key press. Default: null.")
@TestActionArgument(
        name = "key", type = Type.STRING, optional = true,
        description = "The key to press. Must be one of the values from the Selenium "
        + "Keys enumeration (\"ENTER\", \"CONTROL\", \"SHIFT\", etc.). Exactly "
        + "one of the `key` or the `char` arguments must be provided.")
@TestActionArgument(
        name = "char", type = Type.STRING, optional = true,
        description = "The character corresponding to the key to be pressed.")

public class ActionsKeyDown extends SeleniumTestAction {

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
            SeleniumTestAction.getActionsInstance().keyDown(element, key);
        } else {
            SeleniumTestAction.getActionsInstance().keyDown(key);
        }
    }
}

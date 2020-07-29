package com.enquero.driverfactory.selenium_web_util.function;

import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;
import org.openqa.selenium.WebDriver;

public class SwitchToDefaultContent extends SeleniumTestAction {

    public void run(WebDriver driver) {
        super.run();

        try {
            this.driver.switchTo().defaultContent();
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Failed to switch back to the main page content", ex);
        }
    }
}

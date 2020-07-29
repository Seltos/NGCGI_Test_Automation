package com.enquero.driverfactory.selenium_web_util.function;

import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;
import org.openqa.selenium.WebDriver;

public class CloseWindow extends SeleniumTestAction {

    public void run(WebDriver driver) {
        super.run();

        driver.close();
    }
}

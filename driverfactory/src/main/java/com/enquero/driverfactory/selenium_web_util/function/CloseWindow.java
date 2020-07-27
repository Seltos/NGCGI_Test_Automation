package com.enquero.driverfactory.selenium_web_util.function;

import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;

public class CloseWindow extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        driver.close();
    }
}

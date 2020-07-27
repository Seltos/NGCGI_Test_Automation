package com.enquero.driverfactory.selenium_web_util.function;

import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;

public class TakeScreenshot extends SeleniumTestAction {

    public TakeScreenshot() {
        this.writeArgument("$screenshot", "true");
    }
}


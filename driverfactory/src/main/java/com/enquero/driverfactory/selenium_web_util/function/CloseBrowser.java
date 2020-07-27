package com.enquero.driverfactory.selenium_web_util.function;


import com.enquero.driverfactory.selenium_web_util.SeleniumHelper;
import com.enquero.driverfactory.selenium_web_util.SeleniumTestAction;

public class CloseBrowser extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        SeleniumHelper.discardDriver();
    }
}

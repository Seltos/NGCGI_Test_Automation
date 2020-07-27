package com.enquero.driverfactory.web_selenium;

public class CloseBrowser extends SeleniumTestAction{

    @Override
    public void run() {
        super.run();

        SeleniumHelper.discardDriver();
    }
}

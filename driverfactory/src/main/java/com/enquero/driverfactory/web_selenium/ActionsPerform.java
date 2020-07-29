package com.enquero.driverfactory.web_selenium;

public class ActionsPerform extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        this.waitForAsyncCallsToFinish();
        
        SeleniumTestAction.getActionsInstance().perform();
    }
}

package com.enquero.driverfactory.web_selenium;

public class ActionsPause extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        Integer durationMs = this.readIntArgument("durationMs");

        SeleniumTestAction.getActionsInstance().pause(durationMs);
    }
}

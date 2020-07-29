package com.enquero.driverfactory.web_selenium;

public class ActionsMoveByOffset extends SeleniumTestAction {

    @Override
    public void run() {
        super.run();

        Integer xOffset = this.readIntArgument("xOffset", 0);
        Integer yOffset = this.readIntArgument("yOffset", 0);

        SeleniumTestAction.getActionsInstance().moveByOffset(xOffset, yOffset);
    }
}
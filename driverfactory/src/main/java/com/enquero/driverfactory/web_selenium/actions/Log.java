package com.enquero.driverfactory.web_selenium.actions;


import com.enquero.driverfactory.web_selenium.base.TestAction;

/**
 * An action to log information using the test actor's logger.
 */
public class Log extends TestAction {
    @Override
    public void run() {
        super.run();
        
        String text = this.readStringArgument("text");
    
        this.log.info(text);
    }
}
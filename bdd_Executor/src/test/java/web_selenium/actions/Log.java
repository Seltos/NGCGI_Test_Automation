package web_selenium.actions;


import web_selenium.base.TestAction;

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
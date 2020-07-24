package web_selenium.actions;


import web_selenium.base.TestAction;

/**
 * An action that delays execution of the test scenario.
 */
public class Delay extends TestAction {

    @Override
    public void run() {
        super.run();
        
        int seconds = readIntArgument("seconds");

        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException ex) {
        }
    }
}

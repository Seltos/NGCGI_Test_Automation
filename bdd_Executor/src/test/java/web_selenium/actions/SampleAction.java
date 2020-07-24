package web_selenium.actions;


import web_selenium.base.TestAction;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * A sample action.
 */
public class SampleAction extends TestAction {
    // Override the "initialize" method to do execute any initialization logi
    // that needs to happen before the action is ran, but after the action's
    // arguments have been populated
    @Override
    public void initialize() {
        // Nothing to do here for now
    }
    
    @Override
    public void run() {
        super.run();
        
        String arg1 = readStringArgument("arg1", "");
        this.writeOutput("out1", "*** " + arg1 + " ***");
    }
    
    @Override
    public InputStream takeScreenshot() {
        try {
            return new FileInputStream(new File("test-image.png"));
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
}
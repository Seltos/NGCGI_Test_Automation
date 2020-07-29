package com.enquero.driverfactory.web_selenium.testdef;

import java.util.List;

/**
 * Represents a test segment section in a test definition file.
 */
public class TestDefSegment {

    public List<TestDefAction> actions;
    
    @Deprecated
    public Integer index;
    
    @Deprecated
    public Integer step;
    
    public Integer segment;

}
package com.enquero.driverfactory.web_selenium.base;

public class SessionStatusResponse {

    public int currentDataRecordIndex = -1;
    
    public int currentIteration;
    
    public int currentSegmentIndex;
    
    @Deprecated
    public int currentStepIndex;

    public int currentTestIndex;

    public String currentTestPath;

    public String currentTestName;

    public String environment;
    
    public String result;
    
    public String status;
}

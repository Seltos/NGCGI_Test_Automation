package com.enquero.driverfactory.web_selenium.base;

import java.util.HashMap;
import java.util.List;

/**
 * Stores summary information about a test action that will be sent to the sync
 * server to be used for reporting and troubleshooting purposes.
 */
public class TestActionInfo {

    public String action;

    public String actorType;

    public HashMap<String, Object> args;

    public boolean isCheckpoint;

    public String description;

    public int durationMs;

    public String macro;
    
    public List<String> macroStack;

    public String result;

    public String screenshot;

    public int segment;
    
    public String stackTrace;
}
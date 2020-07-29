package com.enquero.driverfactory.web_selenium.testdef;

import java.util.List;

/**
 * Represents an actor section in a test definition file.
 */
public class TestDefActor {

    public String actor;

    /**
     * Alternative name for the "actor" property.
     */
    @Deprecated
    public String actorType;

    public List<TestDefSegment> segments;

    @Deprecated
    public List<TestDefSegment> steps;
}
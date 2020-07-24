package web_selenium.testdef;

import java.util.List;

/**
 * Represents the contents of a test definition file.
 */
public class TestDefinition {
    public List<TestDefActor> actors;
    
    /**
     * This property can be either a string that represents a JS expression evaluating
     * to a JS array or an objects that implements the List interface, when the dataSet
     * property is populated using a YAML sequence (list of items).
     */
    public Object dataSet;
    
    @Deprecated
    public String format;
    
    public String description;
    
    public Object includes;
    
    public List<String> tags;
}

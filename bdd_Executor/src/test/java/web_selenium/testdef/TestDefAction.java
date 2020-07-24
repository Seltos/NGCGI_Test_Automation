/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web_selenium.testdef;

import java.util.Map;

/**
 * Represents an action in a test definition file.
 */
public class TestDefAction {
    public String action;
    
    public Map<String, Object> args;
    
    public String description;
    
    public String macro;
    
    public String script;
    
    @Deprecated
    public Map<String, Object> sharedData;
    
    @Deprecated
    public String type;
}

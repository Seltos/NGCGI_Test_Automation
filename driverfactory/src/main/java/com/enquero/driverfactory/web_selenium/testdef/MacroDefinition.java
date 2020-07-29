package com.enquero.driverfactory.web_selenium.testdef;

import java.util.List;

/**
 * Represents the contents of a macro definition file.
 */
public class MacroDefinition {

    public List<TestDefAction> actions;

    public String description;

    @Deprecated
    public String format;

    /**
     * The fully qualified name of the macro which includes parent directory and
     * the macro definition file name. This property is for internal use only,
     * namely to store the name and relative path of a macro when the macro
     * definition is passed across the call stack.
     */
    public String fullName;

    public Object includes;

    public List<String> tags;
}
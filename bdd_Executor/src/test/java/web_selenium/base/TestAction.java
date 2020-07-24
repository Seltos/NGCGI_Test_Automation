package web_selenium.base;

import com.google.gson.Gson;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import web_selenium.contracts.ILogger;
import web_selenium.contracts.ITestActor;
import web_selenium.exceptions.ArgumentException;
import web_selenium.util.TypeConverter;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.regex.Pattern;


/**
 * Base class for all test actions. Derived classes must override the "run"
 * method to define the specific logic that is performed when the test action
 * executes.
 */
public abstract class TestAction {

    private ITestActor actor;

    private Map<String, Object> args;

    private Set<String> deprecatedArgs;

    private Set<String> deprecatedOutputs;

    /**
     * At runtime, this field will be populated with a reference to the test
     * actor's logger. The text written to this logger is sent both to the
     * console and to the sync service via HTTP.
     */
    protected ILogger log;

    private Map<String, Object> outputs;

    private TestSessionStatus session;

    public TestAction() {
        this.args = new HashMap<>();
        this.deprecatedArgs = new HashSet<>();
        this.deprecatedOutputs = new HashSet<>();
        this.outputs = new HashMap<>();
    }

    /**
     * Returns the list of argument names for the action.
     */
    public String[] getArgNames() {
        if (args == null) {
            return new String[0];
        }

        Set<String> keySet = args.keySet();
        return keySet.toArray(new String[keySet.size()]);
    }

    /**
     * Returns the arguments for this action, as a Map.
     */
    public Map<String, Object> getArgs() {
        if (this.args == null) {
            return new HashMap<String, Object>();
        } else {
            return new HashMap<String, Object>(this.args);
        }
    }

    /**
     * Returns all the output values for this action, as a Map.
     */
    public Map<String, Object> getOutput() {
        return this.outputs;
    }

    public ITestActor getActor() {
        return this.actor;
    }

    public TestSessionStatus getSession() {
        return this.session;
    }

    /**
     * Returns true if the argument with the specified key exists and false
     * otherwise.
     *
     * @param argName The argument name to check for
     */
    public Boolean hasArgument(String argName) {
        return args != null && args.containsKey(argName);
    }

    /**
     * Returns true if the output value with the specified key exists and false
     * otherwise.
     *
     * @param key The output key name to check for
     */
    public Boolean hasOutput(String key) {
        return this.outputs.containsKey(key);
    }

    /**
     * This method is called just before the action is run, but after the action
     * arguments are populated. Derived classes can override it whenever they
     * need to run initialization logic.
     */
    public void initialize() {
    }

    public boolean isArgumentDeprecated(String propName) {
        return this.deprecatedOutputs.contains(propName);
    }

    public boolean isOutputDeprecated(String propName) {
        return this.deprecatedOutputs.contains(propName);
    }

    /**
     * Marks an action argument deprecated, so we can issue warnings in the logs
     * when the value is being used.
     */
    protected void markArgumentAsDeprecated(String key) {
        this.deprecatedArgs.add(key);
    }

    /**
     * Marks an output value as deprecated, so we can issue warnings in the logs
     * when the value is being used.
     */
    protected void markOutputAsDeprecated(String key) {
        this.deprecatedOutputs.add(key);
    }

    /**
     * Reads the argument value for the specified key.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Object readArgument(String argName, Object defaultValue) {
        if (this.hasArgument(argName)) {
            return args.get(argName);
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a string.
     *
     * @param argName The argument whose value will be returned
     */
    public Object readArgument(String argName) {
        if (this.hasArgument(argName)) {
            return args.get(argName);
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as a List.
     *
     * @param klass The type of the array elements
     * @param argName The argument whose value will be returned
     */
    public <T> List<T> readArrayArgument(String argName, Class klass, ArrayList<T> defaultValue) {
        if (this.hasArgument(argName)) {
            return this.toList(argName, args.get(argName), klass);
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a List.
     *
     * @param klass The type of the array elements
     * @param argName The argument whose value will be returned
     */
    public <T> List<T> readArrayArgument(String argName, Class klass) {
        if (this.hasArgument(argName)) {
            return this.<T>toList(argName, args.get(argName), klass);
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as a Boolean.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Boolean readBooleanArgument(String argName, Boolean defaultValue) {
        if (this.hasArgument(argName)) {
            return toBooolean(argName, args.get(argName));
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a Boolean.
     *
     * @param argName The argument whose value will be returned
     */
    public Boolean readBooleanArgument(String argName) {
        if (this.hasArgument(argName)) {
            return toBooolean(argName, args.get(argName));
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as a double.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Double readDoubleArgument(String argName, Double defaultValue) {
        if (this.hasArgument(argName)) {
            return toDouble(argName, args.get(argName));
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a double.
     *
     * @param argName The argument whose value will be returned
     */
    public Double readDoubleArgument(String argName) {
        if (this.hasArgument(argName)) {
            return toDouble(argName, args.get(argName));
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as an integer.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Integer readIntArgument(String argName, Integer defaultValue) {
        if (this.hasArgument(argName)) {
            return toInteger(argName, args.get(argName));
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as an integer.
     *
     * @param argName The argument whose value will be returned
     */
    public Integer readIntArgument(String argName) {
        if (this.hasArgument(argName)) {
            return toInteger(argName, args.get(argName));
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as an integer.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public BufferedImage readImageArgument(String argName, BufferedImage defaultValue) {
        if (this.hasArgument(argName)) {
//            return toInteger(argName, args.get(argName));
            return null;
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as an integer.
     *
     * @param argName The argument whose value will be returned
     */
    public BufferedImage readImageArgument(String argName) {
        if (this.hasArgument(argName)) {
//            return toInteger(argName, args.get(argName));
            return null;
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as a Map.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Map<String, Object> readMapArgument(String argName, Map<String, Object> defaultValue) {
        if (this.hasArgument(argName)) {
            return toMap(argName, args.get(argName));
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a Map.
     *
     * @param argName The argument whose value will be returned
     */
    public Map<String, Object> readMapArgument(String argName) {
        if (this.hasArgument(argName)) {
            return toMap(argName, args.get(argName));
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the output value for the specified key.
     *
     * @param key The key whose value will be returned
     */
    public Object readOutputValue(String key) {
        return this.outputs.get(key);
    }

    /**
     * Reads the argument value for the specified key, as a regular expression
     * pattern.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public Pattern readRegexArgument(String argName, Pattern defaultValue) {
        if (this.hasArgument(argName)) {
            return readRegexArgument(argName);
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a regular expression
     * pattern.
     *
     * @param argName The argument whose value will be returned
     */
    public Pattern readRegexArgument(String argName) {
        if (this.hasArgument(argName)) {
            return TypeConverter.toRegexPattern(args.get(argName));
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Reads the argument value for the specified key, as a string.
     *
     * @param argName The argument whose value will be returned
     * @param defaultValue The default value that will be returned if the
     * specified argument is not populated
     */
    public String readStringArgument(String argName, String defaultValue) {
        if (this.hasArgument(argName)) {
            return toString(argName, args.get(argName));
        } else {
            return defaultValue;
        }
    }

    /**
     * Reads the argument value for the specified key, as a string.
     *
     * @param argName The argument whose value will be returned
     */
    public String readStringArgument(String argName) {
        if (this.hasArgument(argName)) {
            Object argValue = args.get(argName);
            if (argValue instanceof Map) {
                throw new ArgumentException(String.format(
                        "Argument \"%s\" in action %s was expected to be a string but we received an object instead.",
                        argName,
                        this.getClass().getName()));
            } else {
                return toString(argName, argValue);
            }
        } else {
            throw new ArgumentException(String.format(
                    "Mandatory argument \"%s\" was not provided for action %s.",
                    argName,
                    this.getClass().getName()));
        }
    }

    /**
     * Execute the current test action.
     */
    public void run() {
    }

    public void setActor(ITestActor actor) {
        // Session can only be set once
        if (this.actor == null) {
            this.actor = actor;
        } else {
            throw new RuntimeException("An attempt was made to update the test action's actor. The actor can only be set once");
        }
    }

    public void setLogger(ILogger logger) {
        this.log = logger;
    }

    public void setSession(TestSessionStatus session) {
        // Session can only be set once
        if (this.session == null) {
            this.session = session;
        } else {
            throw new RuntimeException("An attempt was made to update the test action session. The session can only be set once");
        }
    }

    /**
     * This method is called by the test actor to take screenshots that are
     * relevant for the current execution context and can be used to
     * troubleshoot failed tests. Derived classes can override it as necessary.
     * The screenshots need to be in PNG format.
     */
    public java.io.InputStream takeScreenshot() {
        return null;
    }

    /**
     * Parses the specified argument value as an ArrayList object of the type
     * specified.
     */
    private <T extends Object> List<T> toList(String argName, Object objValue, Class klass) {
        try {
            if (objValue instanceof List) {
                return (List<T>) objValue;
            } else if (objValue instanceof ScriptObjectMirror) {
//                return (List<T>)ScriptUtils.convert(objValue, Object[].class);
                Object[] objArray = ((ScriptObjectMirror) objValue).to(Object[].class);
                return (List<T>) Arrays.asList(objArray);
            } else if (objValue.getClass().isArray()) {
                ArrayList<T> list = new ArrayList<T>();

                for (T item : (T[]) objValue) {
                    // TODO: find a way to make sure the type of "item" is indeed "T"
                    if (klass.isInstance(item)) {
                        list.add(item);
                    } else {
                        throw new RuntimeException(String.format(
                                "Array element %s does not have the expected type %s.",
                                item,
                                klass.getName()));
                    }
                }
                return list;
            } else {
                List<T> list = new ArrayList<T>();
                list.add((T) objValue);
                return list;
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as an array",
                    argName), ex);
        }
    }

    /**
     * Parses the specified argument value as an Boolean object.
     */
    private Boolean toBooolean(String argName, Object objValue) {
        try {
            return TypeConverter.toBooolean(objValue);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as a boolean",
                    argName));
        }
    }

    /**
     * Parses the specified argument value as an Double object.
     */
    private Double toDouble(String argName, Object objValue) {
        try {
            return Double.parseDouble(String.valueOf(objValue));
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as Double",
                    argName), ex);
        }
    }

    /**
     * Parses the specified argument value as an Integer object.
     */
    private Integer toInteger(String argName, Object objValue) {
        try {
            return TypeConverter.toInteger(objValue);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as an integer",
                    argName));
        }
    }

    /**
     * Parses the specified argument value as a Map object.
     */
    private Map<String, Object> toMap(String argName, Object objValue) {
        try {
            if (objValue instanceof String) {
                // This code should not normally be reached, since map arguments are passed
                // in as maps already by the test actor. This is just a precaution.
                Gson gson = new Gson();
                Map<String, Object> map = new HashMap<String, Object>();
                objValue = gson.fromJson(argName, map.getClass());
            }

            return (Map<String, Object>) objValue;
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as a Map",
                    argName), ex);
        }
    }

    /**
     * Parses the specified argument value as a String object.
     */
    private String toString(String argName, Object objValue) {
        try {
            return TypeConverter.toString(objValue);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse argument %s as a string",
                    argName));
        }
    }

    public void writeArgument(String key, Object value) {
        args.put(key, value);
    }

    /**
     * Writes an output value for the current this.action.
     *
     * @param key
     * @param value
     */
    protected void writeOutput(String key, Object value) {
        this.outputs.put(key, value);
    }
}

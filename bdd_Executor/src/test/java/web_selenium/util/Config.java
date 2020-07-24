package web_selenium.util;

import org.apache.commons.io.FilenameUtils;
import web_selenium.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;

/**
 * Loads properties or YAML configuration files from disk or JAR resources and
 * allows clients to read or modify the configuration properties.
 */
public class Config {

    private Map<String, Object> store;

    public Config() {
        this.store = new HashMap();
    }

    public Config(Map<String, Object> store) {
        this.store = store;
    }

    public Map<String, Object> asMap() {
        return this.store;
    }

    public Object get(String propertyPath) {
        try {
            return getRecursive(propertyPath, this.store);
        } catch (Throwable ex) {
            throw new RuntimeException(String.format(
                    "Failed to read configuration property \"%s\".",
                    propertyPath), ex);
        }
    }

    public Object get(String propertyPath, Object defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return get(propertyPath);
        } else {
            return defaultValue;
        }
    }

    public Boolean getBoolean(String propertyPath) {
        return TypeConverter.toBooolean(get(propertyPath));
    }

    public Boolean getBoolean(String propertyPath, Boolean defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return this.getBoolean(propertyPath);
        } else {
            return defaultValue;
        }
    }

    public Integer getInteger(String propertyPath) {
        return TypeConverter.toInteger(this.get(propertyPath));
    }

    public Integer getInteger(String propertyPath, Integer defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return this.getInteger(propertyPath);
        } else {
            return defaultValue;
        }
    }

    public List getList(String propertyPath) {
        Object propertValue = get(propertyPath);
        if (propertValue instanceof List) {
            return (List) propertValue;
        } else {
            ArrayList<Object> list = new ArrayList<Object>();
            list.add(propertValue);
            return list;
        }
    }

    public List getList(String propertyPath, List defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return this.getList(propertyPath);
        } else {
            return defaultValue;
        }
    }

    public Map getMap(String propertyPath) {
        Object propertValue = get(propertyPath);
        if (propertValue instanceof Map) {
            return (Map) propertValue;
        } else {
            throw new RuntimeException(String.format(
                    "Failed to read the value of %s config property as a map object.",
                    propertyPath));
        }
    }

    public Map getMap(String propertyPath, Map defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return this.getMap(propertyPath);
        } else {
            return defaultValue;
        }
    }

    public String getString(String propertyPath) {
        return TypeConverter.toString(this.get(propertyPath));
    }

    public String getString(String propertyPath, String defaultValue) {
        if (this.hasProperty(propertyPath)) {
            return this.getString(propertyPath);
        } else {
            return defaultValue;
        }
    }

    private Object getRecursive(String propertyPath, Map<String, Object> map) {
        Matcher matcher = StringUtil.executeRegex(propertyPath, "^(?<first>[^\\.]+)\\.?(?<rest>.+)?");

        if (matcher == null) {
            throw new RuntimeException(String.format(
                    "Failed to read configuration property \"%s\".",
                    propertyPath));
        }

        String firstPropertyName = matcher.group("first");
        String restOfExpression = matcher.group("rest");

        if (restOfExpression == null) {
            // Since there were no dots in the property path, it means that we
            // are dealing is a simple property name and not an expression

            if (map.containsKey(firstPropertyName)) {
                return map.get(firstPropertyName);
            } else {
                throw new RuntimeException(String.format(
                        "An attempt was made to read configuration property \"%s\", but this property was not defined.",
                        firstPropertyName));
            }
        } else {
            Object childObject = map.get(firstPropertyName);

            if (childObject instanceof Map) {
                return getRecursive(restOfExpression, (Map) childObject);
            } else {
                throw new RuntimeException(String.format(
                        "An attempt was made to read property \"%s\", from an "
                        + "object that is not a map. The object value was %s.",
                        restOfExpression,
                        childObject));
            }
        }
    }

    private static InputStream getResource(String resourceName) throws ClassNotFoundException {
        Class<?> mainClass = MainUtil.getMainClass();
        return mainClass.getResourceAsStream("/" + resourceName);
    }

    /**
     * Returns true if the specified configuration property exists and false
     * otherwise.
     */
    public boolean hasProperty(String propertyPath) {
        try {
            return hasPropertyRecursive(propertyPath, this.store);
        } catch (Throwable ex) {
            return false;
        }
    }

    private boolean hasPropertyRecursive(String propertyPath, Map<String, Object> map) {
        Matcher matcher = StringUtil.executeRegex(propertyPath, "^(?<first>[^\\.]+)\\.?(?<rest>.+)?");

        if (matcher == null) {
            return false;
        }

        String firstPropertyName = matcher.group("first");
        String restOfExpression = matcher.group("rest");

        if (restOfExpression == null) {
            // Since there were no dots in the property path, it means that we
            // are dealing is a simple property name and not an expression
            return map.containsKey(firstPropertyName);
        } else {
            Object childObject = map.get(firstPropertyName);

            if (childObject instanceof Map) {
                return hasPropertyRecursive(restOfExpression, (Map) childObject);
            } else {
                return false;
            }
        }
    }

    /**
     * Loads a configuration file from the given path or, if this is not
     * specified, from path where the main JAR/class is located.
     */
    public static Config load(String configFileName) {
        String baseName = FilenameUtils.getBaseName(configFileName);
        String propertiesFileName = baseName + ".properties";
        String yamlFileName = baseName + ".yaml";
        String workDir = System.getProperty("user.dir");

        try {
            // Look for properties file in the current path
            File propertiesFile = Paths.get(workDir, propertiesFileName).toFile();
            if (propertiesFile.exists()) {
                Logger.info(String.format("Loading configuration from \"%s\"",
                        propertiesFile.getAbsolutePath()));
                InputStream propertiesInputStream = new FileInputStream(propertiesFile);
                Config config = new Config(readPropertiesFile(propertiesInputStream));
                propertiesInputStream.close();
                return config;
            }

            // Look for YAML file in the current path
            File yamlFile = Paths.get(workDir, yamlFileName).toFile();
            if (yamlFile.exists()) {
                Logger.info(String.format("Loading configuration from \"%s\"",
                        yamlFile.getAbsolutePath()));
                InputStream yamlInputStream = new FileInputStream(yamlFile);
                Config config = new Config(readYamlFile(yamlInputStream));
                yamlInputStream.close();
                return config;
            }

            // Look for YAML file in the resources
            InputStream yamlInputStream = getResource(yamlFileName);
            if (yamlInputStream != null) {
                Logger.info(String.format("Loading configuration from JAR resource file \"%s\"",
                        yamlFileName));
                Config config = new Config(readYamlFile(yamlInputStream));
                yamlInputStream.close();
                return config;
            }

            throw new RuntimeException(String.format(
                    "Failed to find configuration file \"%s\" in current directory or JAR resources.",
                    baseName + ".yaml"));
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to load configuration file \"%s\". Make sure the file exists "
                    + "in the working directory and you have read permissions for it.",
                    configFileName), ex);
        }
    }

    /**
     * Loads configuration data from a YAML string.
     */
    public static Config loadYaml(String yamlData) {
        Object yamlObject = Factory.getYaml().load(yamlData);
        if (yamlObject instanceof Map) {
            return new Config((Map) yamlObject);
        } else {
            throw new RuntimeException(String.format(
                    "Failed to load configuration data in YAML format. The YAML "
                    + "data could not be interpreted as a map."));
        }
    }

    /**
     * Converts a Properties objects into a Map.
     */
    private static Map<String, Object> readPropertiesFile(InputStream fileInputStream) throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        Properties prop = new Properties();
        prop.load(fileInputStream);
        for (final String name : prop.stringPropertyNames()) {
            result.put(name, prop.getProperty(name));
        }
        return result;
    }

    /**
     * Converts a Properties objects into a Map.
     */
    private static Map<String, Object> readYamlFile(InputStream fileInputStream) throws IOException {
        Object yamlObject = Factory.getYaml().load(fileInputStream);
        return (Map<String, Object>) yamlObject;
    }

    public void set(String propertyPath, Object propertyValue) {
        this.store.put(propertyPath, propertyValue);
    }
}

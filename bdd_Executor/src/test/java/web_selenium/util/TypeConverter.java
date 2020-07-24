package web_selenium.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Converts a Java type to a different Java type, using any techniques and
 * tricks necessary (for example the strings "0", "false" and "no" are all
 * considered false when converted to a boolean).
 */
public class TypeConverter {

    /**
     * Converts a string containing JS regex flag characters into an integer
     * with the corresponding bits set, according to the Pattern enum.
     */
    private static int parseRegexFlags(String flagsString) {
        int regexFlags = 0;

        if (flagsString.contains("s")) {
            regexFlags |= Pattern.DOTALL;
        }

        if (flagsString.contains("i")) {
            regexFlags |= Pattern.CASE_INSENSITIVE;
        }

        if (flagsString.contains("m")) {
            regexFlags |= Pattern.MULTILINE;
        }

        return regexFlags;
    }

    /**
     * Parses the specified value as a Boolean object.
     */
    public static Boolean toBooolean(Object objValue) {
        if (objValue == null) {
            return null;
        }
        
        if (objValue instanceof Boolean) {
            return (Boolean) objValue;
        }

        String argValue = objValue.toString().toLowerCase();
        switch (argValue) {
            case "true":
            case "1":
            case "yes":
                return true;
            case "false":
            case "0":
            case "no":
                return false;
            default:
                throw new RuntimeException(String.format(
                        "Failed to parse value %s as a boolean",
                        objValue));
        }
    }

    /**
     * Parses the specified value as a Boolean object. Returns specified default
     * if the value is null.
     */
    public static Boolean toBooolean(Object objValue, Boolean defaultValue) {
        if (objValue != null) {
            return toBooolean(objValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * Parses the specified value as an Integer object.
     */
    public static Integer toInteger(Object objValue) {
        if (objValue == null) {
            return null;
        }

        try {
            if (objValue.getClass() == Integer.class) {
                return (Integer) objValue;
            }

            // We must first parse the argument as a double so that values
            // like "1.0" are valid and properly handled
            Double numberAsDouble = null;
            if (objValue.getClass() == Double.class) {
                numberAsDouble = (Double) objValue;
            } else {
                numberAsDouble = Double.valueOf(String.valueOf(objValue));
            }

            if (Math.floor(numberAsDouble) != numberAsDouble) {
                throw new RuntimeException(String.format(
                        "Failed to parse value %s as an integer",
                        objValue));
            }

            return Math.toIntExact(numberAsDouble.longValue());
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse value %s as an integer",
                    objValue), ex);
        }
    }

    /**
     * Parses the specified value as an Integer object. Returns specified
     * default if the value is null.
     */
    public static Integer toInteger(Object objValue, Integer defaultValue) {
        if (objValue != null) {
            return toInteger(objValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * Parses the specified value as an Integer object.
     */
    public static Pattern toRegexPattern(Object objValue) {
        if (objValue == null) {
            return null;
        }
        
        try {
            String stringValue = toString(objValue).trim();
            if (stringValue.length() == 0) {
                throw new RuntimeException(String.format(
                        "Failed to parse value %s as a regular expression pattern. "
                        + "Converting the value to the string data type resulted "
                        + "in an empty string value.",
                        objValue));
            }

            Pattern parserPattern = Pattern.compile("^\\/(.*?)\\/(.*)?$");
            Matcher matcher = parserPattern.matcher(stringValue);

            if (matcher.find()) {
                return Pattern.compile(matcher.group(1), parseRegexFlags(matcher.group(2)));
            } else {
                return Pattern.compile(stringValue);
            }
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse value %s as a regular expression pattern",
                    objValue), ex);
        }
    }

    /**
     * Parses the specified value as a String object. Returns specified default
     * if the value is null.
     */
    public static Pattern toRegexPattern(Object objValue, Pattern defaultValue) {
        if (objValue != null) {
            return toRegexPattern(objValue);
        } else {
            return defaultValue;
        }
    }

    /**
     * Parses the specified value as a String object.
     */
    public static String toString(Object objValue) {
        if (objValue == null) {
            return null;
        }

        try {
            // The code below is necessary to avoid the ".0" suffix when
            // converting JavaScript whole numbers to strings
            if (objValue instanceof Double) {
                Double doubleVal = (Double) objValue;
                if (doubleVal == Math.ceil(doubleVal)) {
                    return String.valueOf(doubleVal.intValue());
                }
            } else if (objValue instanceof Float) {
                Float floatVal = (Float) objValue;
                if (floatVal == Math.ceil(floatVal)) {
                    return String.valueOf(floatVal.intValue());
                }
            }

            return String.valueOf(objValue);
        } catch (Exception ex) {
            throw new RuntimeException(String.format(
                    "Failed to parse value %s as a string",
                    objValue), ex);
        }
    }

    /**
     * Parses the specified value as a String object. Returns specified default
     * if the value is null.
     */
    public static String toString(Object objValue, String defaultValue) {
        if (objValue != null) {
            return toString(objValue);
        } else {
            return defaultValue;
        }
    }
}

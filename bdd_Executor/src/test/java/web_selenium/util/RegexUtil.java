package web_selenium.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

    /**
     * Parses and returns the flags portion of a JS-style regular expression.
     */
    public static String getRegexFlags(String regex) {
        Pattern parserPattern = Pattern.compile("^\\/(.*?)\\/(.*)?$");
        Matcher matcher = parserPattern.matcher(regex);

        if (matcher.find()) {
            return matcher.group(2).replaceAll("^/+", "");
        } else {
            return "";
        }
    }
}

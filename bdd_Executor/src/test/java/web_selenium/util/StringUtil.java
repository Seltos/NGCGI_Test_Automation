package web_selenium.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * Returns the corresponding Matcher object if the regex matched,
     * or null otherwise.
     */
    public static Matcher executeRegex(String text, String regexString) {
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher;
        } else {
            return null;
        }
    }
    
    /**
     * If the regex contains at least one group, it returns the substring that
     * matched the first group. If there are no groups defined, it returns the
     * full match if the regex matched, or null otherwise.
     */
    public static String substringByRegex(String text, String regexString) {
        Pattern pattern = Pattern.compile(regexString);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            if (matcher.groupCount() > 0) {
                return matcher.group(1);
            } else {
                return matcher.group(0);
            }
        } else {
            return null;
        }
    }
}

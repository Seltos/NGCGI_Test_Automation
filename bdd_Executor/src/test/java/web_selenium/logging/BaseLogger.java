package web_selenium.logging;

import web_selenium.contracts.ILogger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.regex.Pattern;

/**
 * Defines the methods for logging information for each log level and some of
 * the general behavior like the prefixes that should be outputted to the log
 * depending on the log level of the log entry. Derived classes can decide what
 * to do with the logged text by implementing the writeLogEntry method.
 */
public abstract class BaseLogger implements ILogger {

    private boolean isMaskSecrets = true;
    
    private LogLevel level;

    /**
     * RegEx that matches the secrets that will be masked in the log.
     */
    private String secretsRegexp;

    /**
     * Cached RegEx pattern matching the secrets that will be masked in the log.
     */
    private Pattern secretsRegexPattern;

    BaseLogger() {
        this.level = LogLevel.INFO;
    }

    /** Add a secret to be masked in the log. */
    public void addSecret(String secret) {
        if (secretsRegexp == null) {
            secretsRegexp = "\\Q" + secret + "\\E";
        } else {
            secretsRegexp = secretsRegexp + "|\\Q" + secret + "\\E";
        }

        secretsRegexPattern = Pattern.compile(secretsRegexp);
    }
    
    /** Add a secret's regular expression to be masked in the log. */
    public void addSecretByRegex(String newSecretRegexp) {
        if (secretsRegexp == null) {
            secretsRegexp = newSecretRegexp;
        } else {
            secretsRegexp = secretsRegexp + "|" + newSecretRegexp;
        }

        secretsRegexPattern = Pattern.compile(secretsRegexp);
    }

    /** Clear all secrets to be masked in the log. */
    public void clearSecrets() {
        this.secretsRegexp = null;
        this.secretsRegexPattern = null;
    }

    @Override
    public void debug(String text) {
        if (level.getValue() <= LogLevel.DEBUG.getValue()) {
            text = maskSecrets(text);
            writeLogEntry(text, LogLevel.DEBUG);
        }
    }

    @Override
    public void error(String text) {
        if (level.getValue() <= LogLevel.ERROR.getValue()) {
            text = maskSecrets(text);
            writeLogEntry(text, LogLevel.ERROR);
        }
    }

    @Override
    public void error(String message, Throwable exception) {
        this.error(message);
        this.error(BaseLogger.getStackTrace(exception));
    }

    public LogLevel getLevel() {
        return level;
    }

    public String getLevelAsString(LogLevel level) {
        String levelAsString;

        switch (level) {
            case DEBUG:
                levelAsString = "debug";
                break;
            case ERROR:
                levelAsString = "error";
                break;
            case INFO:
                levelAsString = "info";
                break;
            case TRACE:
                levelAsString = "trace";
                break;
            case WARN:
                levelAsString = "warn";
                break;
            default:
                levelAsString = "info";
        }

        return levelAsString;
    }

    protected String getPrefixForLevel(LogLevel level) {
        String prefix;

        switch (level) {
            case DEBUG:
                prefix = "DEBUG: ";
                break;
            case INFO:
                prefix = "";
                break;
            case ERROR:
                prefix = "ERROR: ";
                break;
            case WARN:
                prefix = "WARN: ";
                break;
            case TRACE:
                prefix = "TRACE: ";
                break;
            default:
                prefix = "";
        }

        return prefix;
    }

    public static String getStackTrace(Throwable exception) {
        StringWriter sw = new StringWriter();
        exception.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Override
    public void info(String text) {
        if (level.getValue() <= LogLevel.INFO.getValue()) {
            text = maskSecrets(text);
            writeLogEntry(text, LogLevel.INFO);
        }
    }

    private String maskSecrets(String text) {
        if (this.secretsRegexPattern == null || !this.isMaskSecrets) {
            return text;
        } else {
            return this.secretsRegexPattern.matcher(text).replaceAll("<<MASKED_SECRET>>");
        }
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }
    
    public void setMaskSecrets(boolean isMaskSecrets) {
        this.isMaskSecrets = isMaskSecrets;
    }

    @Override
    public void trace(String text) {
        if (level.getValue() <= LogLevel.TRACE.getValue()) {
            text = maskSecrets(text);
            writeLogEntry(text, LogLevel.TRACE);
        }
    }

    @Override
    public void warning(String text) {
        if (level.getValue() <= LogLevel.WARN.getValue()) {
            text = maskSecrets(text);
            writeLogEntry(text, LogLevel.WARN);
        }
    }

    @Override
    public void warning(String message, Throwable exception) {
        this.warning(message);
        this.warning(BaseLogger.getStackTrace(exception));
    }

    protected void writeLogEntry(String text, LogLevel level) {
        writeLogEntry(text, level, Instant.now());
    };
    
    protected abstract void writeLogEntry(String text, LogLevel level, Instant timestamp);
}

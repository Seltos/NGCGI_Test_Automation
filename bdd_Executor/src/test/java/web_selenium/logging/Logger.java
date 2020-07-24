package web_selenium.logging;


import web_selenium.contracts.ILogger;

public class Logger {

    private static ILogger INSTANCE;

    static {
        INSTANCE = new ConsoleLogger();
    }

    public static ILogger getLogger() {
        return INSTANCE;
    }

    public static void debug(String text) {
        INSTANCE.debug(text);
    }
    
    public static void error(String text) {
        INSTANCE.error(text);
    }

    public static void error(Throwable exception) {
        INSTANCE.error(BaseLogger.getStackTrace(exception));
    }
    
    public static void error(String message, Throwable exception) {
        INSTANCE.error(message, exception);
    }

    public static void info(String text) {
        INSTANCE.info(text);
    }
    
    public static void setLogger(ILogger loger) {
        INSTANCE = loger;
    }

    public static void trace(String text) {
        INSTANCE.trace(text);
    }
    
    public static void warning(String text) {
        INSTANCE.warning(text);
    }
    
    public static void warning(String message, Throwable exception) {
        INSTANCE.warning(message);
        INSTANCE.warning(BaseLogger.getStackTrace(exception));
    }
}

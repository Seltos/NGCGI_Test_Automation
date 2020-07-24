package web_selenium.contracts;


import web_selenium.logging.LogLevel;

public interface ILogger {

    public void addSecret(String secret);
    
    public void addSecretByRegex(String secretRegex);
    
    public void clearSecrets();
            
    public void debug(String text);
    
    public void error(String text);
    
    public void error(String message, Throwable exception);

    public void info(String text);
    
    public void setLevel(LogLevel level);

    public void setMaskSecrets(boolean isMaskSecrets);
            
    public void trace(String text);
    
    public void warning(String text);
    
    public void warning(String message, Throwable exception);
}

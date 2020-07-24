package web_selenium.exceptions;

/**
 * Thrown when there's a problem with a test action's argument (missing
 * mandatory argument, wrong argument data type, etc.).
 */
public class ArgumentException extends RuntimeException {
    public ArgumentException(String message) {
        super(message);
    }
}

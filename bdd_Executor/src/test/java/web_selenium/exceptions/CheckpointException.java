package web_selenium.exceptions;

/**
 * Wraps exceptions returned from checkpoint actions.
 */
public class CheckpointException extends RuntimeException {
    public CheckpointException(Throwable cause) {
        super(cause);
    }
}

package com.enquero.driverfactory.web_selenium.exceptions;

/**
 * Thrown when a script calls the $fail function to deliberately fail a test.
 */
public class IntentionalFailException extends RuntimeException {
    public IntentionalFailException(String message) {
        super(message);
    }
}
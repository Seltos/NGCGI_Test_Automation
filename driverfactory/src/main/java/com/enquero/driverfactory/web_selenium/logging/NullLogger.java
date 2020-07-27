package com.enquero.driverfactory.web_selenium.logging;

import java.time.Instant;

/**
 * A logger that doesn't do anything with the log entries. This is
 * used when we need to avoid logging sensitive information.
 */
public class NullLogger extends BaseLogger {

    @Override
    protected void writeLogEntry(String text, LogLevel level, Instant timestamp) {
        // Nothing to do here.
    }
}

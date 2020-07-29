package com.enquero.driverfactory.web_selenium.logging;

import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * A logger that outputs to the console.
 */
public class ConsoleLogger extends BaseLogger {

    @Override
    protected void writeLogEntry(String text, LogLevel level, Instant timestamp) {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
        
        if (text != null && !text.isEmpty()) {
            DateTimeFormatter isoTimeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");
            String timeOfLogEntry = LocalTime.from(timestamp.atZone(ZoneId.systemDefault())).format(isoTimeFormatter);
            String message = String.format("%s %s%s", timeOfLogEntry, getPrefixForLevel(level), text);
            System.out.println(message);
        } else {
            System.out.println();
        }
    }
}
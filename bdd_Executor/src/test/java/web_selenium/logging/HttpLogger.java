package web_selenium.logging;

import com.google.gson.JsonObject;
import web_selenium.http.ContentType;
import web_selenium.http.HttpRequest;
import web_selenium.http.HttpRequestOptions;
import web_selenium.http.HttpVerb;
import web_selenium.util.Config;
import web_selenium.util.ServerRequest;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Utility class to create log entries using the sync service HTTP API
 */
public class HttpLogger extends BaseLogger {

    private LinkedBlockingDeque<LogEntry> logEntries;

    private Thread logEntriesUploadThread;

    HttpRequest request;

    private String serverUrl;

    private String testSessionId;

    public HttpLogger(String serverUrl, String testSessionId, HashMap<String, String> context, Config config) {
        this.serverUrl = serverUrl;
        this.testSessionId = testSessionId;

        HttpRequestOptions options = new HttpRequestOptions(
                String.format("%s/api/session/%s/log",
                        this.serverUrl,
                        this.testSessionId),
                HttpVerb.POST);
        this.request = new ServerRequest(options, config);
        this.logEntries = new LinkedBlockingDeque<>();
        this.logEntriesUploadThread = new Thread() {
            public void run() {
                while (true) {
                    try {
                        LogEntry entry = logEntries.takeFirst();
                        
                        String timeOfLogEntry = OffsetDateTime.ofInstant(entry.getTimestamp(), ZoneId.of("GMT")).format(DateTimeFormatter.ISO_DATE_TIME);
                        
                        JsonObject json = new JsonObject();
                        json.addProperty("level", getLevelAsString(entry.getLogLevel()));
                        json.addProperty("message", entry.getText());
                        json.addProperty("time", timeOfLogEntry);

                        if (context != null) {
                            JsonObject extras = new JsonObject();
                            Iterator<Entry<String, String>> it = context.entrySet().iterator();
                            while (it.hasNext()) {
                                Entry<String, String> pair = it.next();
                                extras.addProperty(pair.getKey(), pair.getValue());
                            }
                            json.add("extras", extras);
                        }
                        request.setContent(json.toString(), ContentType.APPLICATION_JSON);
                        try {
                            request.execute();
                        } catch (IOException exc) {
                            System.out.println("ERROR: Failed sending log entry to server");
                        }

                    } catch (Exception ex) {
                        System.out.println(String.format("ERROR: Failed to process log entries. %s", ex.getMessage()));
                        // Eat all exceptions
                    }
                }
            }
        };
        logEntriesUploadThread.start();
    }

    @Override
    protected void writeLogEntry(String text, LogLevel level, Instant timestamp) {
        String httpMessage;
        String consoleMessage;

        if (timestamp == null) {
            timestamp = Instant.now();
        }

        if (text != null && !text.isEmpty()) {
            DateTimeFormatter isoTimeFormatter = DateTimeFormatter.ofPattern("HH.mm.ss");
            String timeOfLogEntry = LocalTime.from(timestamp.atZone(ZoneId.systemDefault())).format(isoTimeFormatter);
            httpMessage = String.format("%s%s", getPrefixForLevel(level), text);
            consoleMessage = String.format("%s %s", timeOfLogEntry, httpMessage);
        } else {
            httpMessage = "";
            consoleMessage = "";
        }

        // Also write the message to console
        System.err.println(consoleMessage);

        logEntries.add(new LogEntry(level, httpMessage, timestamp));
    }
}

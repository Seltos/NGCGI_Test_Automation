package web_selenium.logging;

import lombok.*;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogEntry {

    private LogLevel logLevel;
    private String text;
    private Instant timestamp;

}

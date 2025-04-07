package pl.edu.pwr.ztw.books.activities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ActivityLog {
    private static final ConcurrentLinkedQueue<ActivityLog> logs = new ConcurrentLinkedQueue<>();
    private static final int MAX_LOG_ENTRIES = 100;

    private final String message;
    private final LocalDateTime timestamp;

    public ActivityLog(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        addLog(this);
    }

    private static synchronized void addLog(ActivityLog log) {
        logs.add(log);
        if (logs.size() > MAX_LOG_ENTRIES) {
            logs.poll();
        }
    }

    public static Collection<ActivityLog> getRecentActivity() {
        return new ArrayList<>(logs);
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

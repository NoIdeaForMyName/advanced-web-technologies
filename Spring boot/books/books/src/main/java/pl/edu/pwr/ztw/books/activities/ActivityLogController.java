package pl.edu.pwr.ztw.books.activities;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/activity")
public class ActivityLogController {

    @GetMapping
    public ResponseEntity<Collection<ActivityLog>> getActivityLogs() {
        Collection<ActivityLog> logs = ActivityLog.getRecentActivity().stream()
                .sorted(Comparator.comparing(ActivityLog::getTimestamp).reversed())
                .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }
}

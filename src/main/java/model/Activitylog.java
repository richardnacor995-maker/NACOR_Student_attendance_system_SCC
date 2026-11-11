package model;

import java.sql.Timestamp;

public record Activitylog(
        int logId,
        int userId,
        String actionType,
        String description,
        Timestamp timestamp
) {}
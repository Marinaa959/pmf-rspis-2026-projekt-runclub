package com.infobip.pmf.course.runclub.event;

import java.time.LocalDateTime;

public record Event(
        Long id,
        Long clubId,
        String name,
        LocalDateTime startTime,
        double distanceMeters,
        double elevationGainMeters
) {
}

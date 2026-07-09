package com.infobip.pmf.course.runclub.signup;

import java.time.LocalDateTime;

public record Signup(
        Long id,
        String runnerId,
        Long eventId,
        LocalDateTime createdAt
) {
}

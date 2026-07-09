package com.infobip.pmf.course.runclub.club;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Club(
        Long id,
        @NotBlank String name,
        @NotBlank String city,
        @NotNull ClubLevel level,
        String description
) {
}

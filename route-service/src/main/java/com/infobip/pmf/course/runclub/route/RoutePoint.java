package com.infobip.pmf.course.runclub.route;

import jakarta.validation.constraints.NotNull;

public record RoutePoint(
        @NotNull Double lat,
        @NotNull Double lng
) {
}

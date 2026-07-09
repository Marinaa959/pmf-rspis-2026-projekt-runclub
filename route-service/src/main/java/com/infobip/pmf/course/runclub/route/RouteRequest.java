package com.infobip.pmf.course.runclub.route;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.stream.Collectors;

public record RouteRequest(
        @NotNull @Size(min = 2) List<@Valid RoutePoint> coordinates
) {

    public String cacheKey() {
        return coordinates.stream()
                .map(p -> p.lat() + "," + p.lng())
                .collect(Collectors.joining(";"));
    }
}

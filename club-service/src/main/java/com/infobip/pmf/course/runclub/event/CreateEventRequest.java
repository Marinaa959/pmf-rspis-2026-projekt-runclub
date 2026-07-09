package com.infobip.pmf.course.runclub.event;

import com.infobip.pmf.course.runclub.route.RoutePoint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;


public record CreateEventRequest(
        @NotBlank String name,
        @NotNull LocalDateTime startTime,
        @NotNull @Size(min = 2) List<@Valid RoutePoint> route  // koordinate rute
) {
}

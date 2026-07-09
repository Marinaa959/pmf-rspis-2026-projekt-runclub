package com.infobip.pmf.course.runclub.route;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "club-service")
public record AppProperties(@NotNull @Valid RouteService routeService) {

    public record RouteService(@NotBlank String baseUrl, @NotNull Integer timeoutInSeconds) {
    }
}

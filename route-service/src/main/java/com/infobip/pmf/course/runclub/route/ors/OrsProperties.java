package com.infobip.pmf.course.runclub.route.ors;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

// eksterna konfiguracija za OpenRouteService (po uzoru na AppProperties iz primjera otpornosti).
// vrijednosti dolaze iz application.yaml
@Validated
@ConfigurationProperties(prefix = "route-service.ors")
public record OrsProperties(
        @NotBlank String baseUrl,
        @NotBlank String apiKey,
        @NotBlank String profile,
        @NotNull Integer timeoutInSeconds
) {
}

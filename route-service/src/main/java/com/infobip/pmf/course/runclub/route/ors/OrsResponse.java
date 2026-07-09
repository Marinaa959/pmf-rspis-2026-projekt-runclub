package com.infobip.pmf.course.runclub.route.ors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

// djelomični model ORS odgovora; zanimaju nas samo duljina  i uspon

@JsonIgnoreProperties(ignoreUnknown = true)
public record OrsResponse(List<OrsRoute> routes) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OrsRoute(OrsSummary summary) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OrsSummary(double distance, Double ascent) {
    }
}

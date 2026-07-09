package com.infobip.pmf.course.runclub.route.ors;

import com.infobip.pmf.course.runclub.route.RouteInfo;
import com.infobip.pmf.course.runclub.route.RouteRequest;
import com.infobip.pmf.course.runclub.route.exception.RouteCalculationException;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.util.List;

// HTTP klijent prema OpenRouteService API-ju
@Component
public class OrsClient {

    private final RestClient restClient;
    private final String profile;

    public OrsClient(OrsProperties properties) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(properties.timeoutInSeconds()));
        requestFactory.setReadTimeout(Duration.ofSeconds(properties.timeoutInSeconds()));
        this.profile = properties.profile();
        this.restClient = RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader("Authorization", properties.apiKey())
                .requestFactory(requestFactory)
                .build();
    }

    public RouteInfo calculate(RouteRequest request) {
        try {
            // ORS očekuje koordinate kao [lng, lat]
            var coordinates = request.coordinates().stream()
                    .map(p -> List.of(p.lng(), p.lat()))
                    .toList();

            var response = restClient.post()
                    .uri("/v2/directions/{profile}/json", profile)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new OrsRequest(coordinates, true))
                    .retrieve()
                    .body(OrsResponse.class);

            if (response == null || response.routes() == null || response.routes().isEmpty()) {
                throw new RouteCalculationException("OpenRouteService returned no route.", null);
            }

            var summary = response.routes().getFirst().summary();
            double ascent = summary.ascent() != null ? summary.ascent() : 0.0;
            return new RouteInfo(summary.distance(), ascent);

        } catch (RouteCalculationException e) {
            throw e;
        } catch (Exception e) {
            throw new RouteCalculationException("Failed to calculate route via OpenRouteService.", e);
        }
    }

    // tijelo ORS zahtjeva
    private record OrsRequest(List<List<Double>> coordinates, boolean elevation) {
    }
}

package com.infobip.pmf.course.runclub.route;

import com.infobip.pmf.course.runclub.route.exception.InvalidRouteException;
import com.infobip.pmf.course.runclub.route.exception.RouteServiceException;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.time.Duration;
import java.util.List;

@Component
public class RouteClient {

    private static final String ENDPOINT = "/routes";

    private final RestClient restClient;

    public RouteClient(AppProperties properties) {
        var requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(properties.routeService().timeoutInSeconds()));
        requestFactory.setReadTimeout(Duration.ofSeconds(properties.routeService().timeoutInSeconds()));
        this.restClient = RestClient.builder()
                .baseUrl(properties.routeService().baseUrl())
                .requestFactory(requestFactory)
                .build();
    }

    public RouteInfo calculate(List<RoutePoint> coordinates) {
        try {
            return restClient.post()
                    .uri(ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new CalculateRouteRequest(coordinates))
                    .retrieve()
                    .toEntity(RouteInfo.class)
                    .getBody();
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().is4xxClientError()) {
                throw new InvalidRouteException("Route was rejected by route service.");
            }
            throw new RouteServiceException("Route service returned an error.", exception);
        } catch (Exception exception) {
            throw new RouteServiceException("Route service is unavailable.", exception);
        }
    }

    public record CalculateRouteRequest(List<RoutePoint> coordinates) {
    }
}

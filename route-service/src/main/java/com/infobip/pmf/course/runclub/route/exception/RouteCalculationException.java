package com.infobip.pmf.course.runclub.route.exception;

// poziv prema OpenRouteServiceu nije uspio
public class RouteCalculationException extends RuntimeException {
    public RouteCalculationException(String message, Throwable cause) {
        super(message, cause);
    }
}

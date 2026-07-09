package com.infobip.pmf.course.runclub.api;

import com.infobip.pmf.course.runclub.route.exception.RouteCalculationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlers.class);

    // Neispravni ulaz
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handle(MethodArgumentNotValidException e) {
        var message = e.getFieldErrors().stream()
                .map(fe -> "%s: %s".formatted(fe.getField(), fe.getDefaultMessage()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation error.");
        return ResponseEntity.badRequest().body(new Error(message));
    }

    // ORS nedostupan
    @ExceptionHandler(RouteCalculationException.class)
    public ResponseEntity<Error> handle(RouteCalculationException e) {
        LOGGER.error("Route calculation failed.", e);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(new Error("Could not calculate route via upstream provider."));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handle(Exception e) {
        LOGGER.error("Unexpected error.", e);
        return ResponseEntity.internalServerError().body(new Error("An unexpected error occurred."));
    }

    public record Error(String message) {
    }
}

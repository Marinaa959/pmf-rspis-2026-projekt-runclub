package com.infobip.pmf.course.runclub.api;

import com.infobip.pmf.course.runclub.club.exception.ClubNotFoundException;
import com.infobip.pmf.course.runclub.club.exception.DuplicateClubException;
import com.infobip.pmf.course.runclub.event.exception.EventNotFoundException;
import com.infobip.pmf.course.runclub.route.exception.InvalidRouteException;
import com.infobip.pmf.course.runclub.route.exception.RouteServiceException;
import com.infobip.pmf.course.runclub.runner.exception.DuplicateRunnerException;
import com.infobip.pmf.course.runclub.runner.exception.ForbiddenRunnerUpdateException;
import com.infobip.pmf.course.runclub.runner.exception.RunnerNotFoundException;
import com.infobip.pmf.course.runclub.signup.exception.DuplicateSignupException;
import jakarta.validation.ValidationException;
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

    @ExceptionHandler(ClubNotFoundException.class)
    public ResponseEntity<Error> handle(ClubNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(RunnerNotFoundException.class)
    public ResponseEntity<Error> handle(RunnerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Error> handle(EventNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(e.getMessage()));
    }


    @ExceptionHandler(DuplicateClubException.class)
    public ResponseEntity<Error> handle(DuplicateClubException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(DuplicateRunnerException.class)
    public ResponseEntity<Error> handle(DuplicateRunnerException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(ForbiddenRunnerUpdateException.class)
    public ResponseEntity<Error> handle(ForbiddenRunnerUpdateException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
    }

    @ExceptionHandler(DuplicateSignupException.class)
    public ResponseEntity<Error> handle(DuplicateSignupException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new Error(e.getMessage()));
    }


    @ExceptionHandler(InvalidRouteException.class)
    public ResponseEntity<Error> handle(InvalidRouteException e) {
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }

    // Validacija tijela zahtjeva
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Error> handle(MethodArgumentNotValidException e) {
        var message = e.getFieldErrors().stream()
                .map(fieldError -> "%s: %s".formatted(fieldError.getField(), fieldError.getDefaultMessage()))
                .reduce((a, b) -> a + ", " + b)
                .orElse("Validation error.");
        return ResponseEntity.badRequest().body(new Error(message));
    }

    // Validacija parametara putanje
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Error> handle(ValidationException e) {
        return ResponseEntity.badRequest().body(new Error(e.getMessage()));
    }

    @ExceptionHandler(RouteServiceException.class)
    public ResponseEntity<Error> handle(RouteServiceException e) {
        LOGGER.error("Route service call failed.", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new Error("Route service is currently unavailable. Try again later."));
    }

    // Sve ostale greške

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handle(Exception e) {
        LOGGER.error("Unexpected error.", e);
        return ResponseEntity.internalServerError().body(new Error("An unexpected error occurred."));
    }

    public record Error(String message) {
    }
}

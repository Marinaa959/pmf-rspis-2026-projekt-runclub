package com.infobip.pmf.course.runclub.signup.exception;

public class DuplicateSignupException extends RuntimeException {
    public DuplicateSignupException(String runnerId, Long eventId) {
        super("Runner '%s' is already signed up for event %d.".formatted(runnerId, eventId));
    }
}

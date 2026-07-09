package com.infobip.pmf.course.runclub.event.exception;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event with id %d does not exist.".formatted(id));
    }
}

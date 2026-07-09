package com.infobip.pmf.course.runclub.club.exception;

public class ClubNotFoundException extends RuntimeException {
    public ClubNotFoundException(Long id) {
        super("Club with id %d does not exist.".formatted(id));
    }
}

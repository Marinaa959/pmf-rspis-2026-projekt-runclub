package com.infobip.pmf.course.runclub.club.exception;

public class DuplicateClubException extends RuntimeException {
    public DuplicateClubException(String name) {
        super("Club with name '%s' already exists.".formatted(name));
    }
}

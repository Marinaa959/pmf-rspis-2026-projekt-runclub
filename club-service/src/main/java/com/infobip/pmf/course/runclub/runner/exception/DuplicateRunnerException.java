package com.infobip.pmf.course.runclub.runner.exception;

public class DuplicateRunnerException extends RuntimeException {
    public DuplicateRunnerException(String id) {
        super("Runner with id '%s' already exists.".formatted(id));
    }
}

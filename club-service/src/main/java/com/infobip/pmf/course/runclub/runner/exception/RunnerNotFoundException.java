package com.infobip.pmf.course.runclub.runner.exception;

public class RunnerNotFoundException extends RuntimeException {
    public RunnerNotFoundException(String id) {
        super("Runner with id '%s' does not exist.".formatted(id));
    }
}

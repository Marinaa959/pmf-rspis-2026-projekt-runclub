package com.infobip.pmf.course.runclub.runner.exception;

public class ForbiddenRunnerUpdateException extends RuntimeException {
    public ForbiddenRunnerUpdateException() {
        super("Updating runner's id is not allowed.");
    }
}

package com.sprinklr.unittesttracker.exception;

public class InvalidReportException extends RuntimeException {
    public InvalidReportException(String message) {
        super(message);
    }
}

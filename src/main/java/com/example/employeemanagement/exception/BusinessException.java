package com.example.employeemanagement.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    public BusinessException(String message) {
        this(message, HttpStatus.BAD_REQUEST, "BUSINESS_RULE_VIOLATION");
    }

    public BusinessException(String message, HttpStatus status) {
        this(message, status, "BUSINESS_RULE_VIOLATION");
    }

    public BusinessException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status != null ? status : HttpStatus.BAD_REQUEST;
        this.errorCode = errorCode != null ? errorCode : "BUSINESS_RULE_VIOLATION";
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
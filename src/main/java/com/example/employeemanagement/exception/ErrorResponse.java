package com.example.employeemanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        boolean success,
        Instant timestamp,
        int status,
        String error,
        String errorCode,
        String message,
        String path,
        List<FieldErrorDetail> fieldErrors
) {
    public record FieldErrorDetail(String field, String message) {}

    public static ErrorResponse of(int status, String error, String errorCode, String message, String path) {
        return new ErrorResponse(
                false,
                Instant.now(),
                status,
                error,
                errorCode,
                message,
                path,
                null
        );
    }

    public static ErrorResponse ofValidation(int status, String error, String message, String path, List<FieldErrorDetail> fieldErrors) {
        return new ErrorResponse(
                false,
                Instant.now(),
                status,
                error,
                "VALIDATION_FAILED",
                message,
                path,
                fieldErrors
        );
    }
}
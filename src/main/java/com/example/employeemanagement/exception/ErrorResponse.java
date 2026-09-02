package com.example.employeemanagement.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        boolean success,
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
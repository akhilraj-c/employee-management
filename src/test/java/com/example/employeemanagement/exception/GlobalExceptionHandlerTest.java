package com.example.employeemanagement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
        when(request.getRequestURI()).thenReturn("/api/v1/test");
    }

    @Test
    @DisplayName("Should handle ResourceNotFoundException with 404 and RESOURCE_NOT_FOUND error code")
    void handleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Employee not found: 10");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleResourceNotFound(ex, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().success());
        assertEquals(404, response.getBody().status());
        assertEquals("RESOURCE_NOT_FOUND", response.getBody().errorCode());
        assertEquals("Employee not found: 10", response.getBody().message());
        assertEquals("/api/v1/test", response.getBody().path());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    @DisplayName("Should handle default BusinessException with 400 and BUSINESS_RULE_VIOLATION error code")
    void handleBusinessException_Default() {
        BusinessException ex = new BusinessException("Joining date not valid");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().status());
        assertEquals("BUSINESS_RULE_VIOLATION", response.getBody().errorCode());
        assertEquals("Joining date not valid", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle custom status BusinessException (e.g. 409 CONFLICT)")
    void handleBusinessException_CustomStatus() {
        BusinessException ex = new BusinessException(
                "Department already exists",
                HttpStatus.CONFLICT,
                "DUPLICATE_DEPARTMENT"
        );

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleBusinessException(ex, request);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(409, response.getBody().status());
        assertEquals("DUPLICATE_DEPARTMENT", response.getBody().errorCode());
        assertEquals("Department already exists", response.getBody().message());
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException with structured field errors")
    void handleValidationException() {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "name", "Name is mandatory"));
        bindingResult.addError(new FieldError("testObject", "email", "Invalid email format"));

        MethodParameter parameter = MockitoAnnotations.class.getDeclaredMethods()[0].getParameters().length > 0
                ? MethodParameter.forParameter(MockitoAnnotations.class.getDeclaredMethods()[0].getParameters()[0])
                : null;

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleValidationException(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("VALIDATION_FAILED", response.getBody().errorCode());
        assertNotNull(response.getBody().fieldErrors());
        assertEquals(2, response.getBody().fieldErrors().size());
        assertEquals("name", response.getBody().fieldErrors().get(0).field());
        assertEquals("Name is mandatory", response.getBody().fieldErrors().get(0).message());
    }

    @Test
    @DisplayName("Should handle MethodArgumentTypeMismatchException with 400 and TYPE_MISMATCH error code")
    void handleTypeMismatch() {
        MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException(
                "abc", Long.class, "employeeId", null, new IllegalArgumentException("Type mismatch")
        );

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleTypeMismatch(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("TYPE_MISMATCH", response.getBody().errorCode());
        assertTrue(response.getBody().message().contains("employeeId"));
        assertTrue(response.getBody().message().contains("Long"));
    }

    @Test
    @DisplayName("Should handle HttpRequestMethodNotSupportedException with 405 METHOD_NOT_ALLOWED")
    void handleMethodNotSupported() {
        HttpRequestMethodNotSupportedException ex = new HttpRequestMethodNotSupportedException("POST");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodNotSupported(ex, request);

        assertEquals(HttpStatus.METHOD_NOT_ALLOWED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("METHOD_NOT_SUPPORTED", response.getBody().errorCode());
    }

    @Test
    @DisplayName("Should handle HttpMediaTypeNotSupportedException with 415 UNSUPPORTED_MEDIA_TYPE")
    void handleMediaTypeNotSupported() {
        HttpMediaTypeNotSupportedException ex = new HttpMediaTypeNotSupportedException("Unsupported content-type text/plain");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMediaTypeNotSupported(ex, request);

        assertEquals(HttpStatus.UNSUPPORTED_MEDIA_TYPE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("UNSUPPORTED_MEDIA_TYPE", response.getBody().errorCode());
    }

    @Test
    @DisplayName("Should handle unhandled Exception with 500 INTERNAL_SERVER_ERROR")
    void handleUnhandledException() {
        NullPointerException ex = new NullPointerException("Unexpected null reference");

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleUnhandledException(ex, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().status());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().errorCode());
        assertEquals("An unexpected internal error occurred. Please contact support if the issue persists.", response.getBody().message());
    }
}

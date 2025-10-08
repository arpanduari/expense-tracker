package dev.arpan.expensetracker.exception;

import dev.arpan.expensetracker.exception.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author arpan
 * @since 8/3/25
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, WebRequest webRequest, HttpStatus status) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .apiPath(webRequest.getDescription(false))
                .statusCode(status.value())
                .status(status.getReasonPhrase())
                .errorMessage(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(errorResponse);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Method not allowed");
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> response.put(fieldError.getField(), fieldError.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> response = new HashMap<>();
        ex.getConstraintViolations()
                .forEach(constraintViolation -> response.put(constraintViolation.getPropertyPath()
                        .toString(), constraintViolation.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = "Duplicate entry";

        Map<String, String> response = new HashMap<>();
        response.put("error", message);

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
        return buildErrorResponse(exception, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        return buildErrorResponse(exception, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(PasswordResetTokenAlreadySentException.class)
    public ResponseEntity<ErrorResponse> handlePasswordResetTokenAlreadySentException(PasswordResetTokenAlreadySentException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordNotMatchingException.class)
    public ResponseEntity<ErrorResponse> handlerPasswordNotMatchingException(PasswordNotMatchingException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordPolicyViolationException.class)
    public ResponseEntity<ErrorResponse> handlerPasswordPolicyViolation(PasswordPolicyViolationException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException exception, WebRequest request) {
        return buildErrorResponse(exception, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OTPNotVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleOtpNotVerifiedException(OTPNotVerifiedException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.LOCKED);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception, WebRequest webRequest) {
        return buildErrorResponse(exception, webRequest, HttpStatus.CONFLICT);
    }
}

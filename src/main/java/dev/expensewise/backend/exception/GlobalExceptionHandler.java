package dev.expensewise.backend.exception;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author arpan
 * @since 8/3/25
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b");
    private static final Pattern UUID_PATTERN =
            Pattern.compile("\\b[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}\\b");
    private static final Pattern NUMERIC_ID_PATTERN = Pattern.compile("\\b(id|ID|userId|user_id)\\s*[:=]?\\s*\\d+\\b");

    private final ErrorCodeMapper errorCodeMapper;

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.METHOD_NOT_ALLOWED);
        problem.setTitle("Method Not Allowed");
        problem.setDetail(ex.getMessage());

        problem.setProperty("supportedMethods", ex.getSupportedHttpMethods());
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("path", request.getDescription(false));

        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("path", request.getDescription(false));

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        problem.setProperty("errors", errors);
        return problem;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Failed");
        problem.setDetail("One or more constraints were violated");

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));

        problem.setProperty("errors", errors);
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("path", request.getDescription(false));

        return problem;
    }

    @ExceptionHandler({
        PasswordNotMatchingException.class,
        PasswordPolicyViolationException.class,
        PasswordResetTokenAlreadySentException.class
    })
    public ProblemDetail handleBadPasswordRequest(RuntimeException ex, WebRequest request) {
        return build(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ResourceNotFoundException.class, ResourceExpiredException.class})
    public ProblemDetail handleNotFound(RuntimeException ex, WebRequest request) {
        return build(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        UsernameAlreadyExistsException.class,
        EmailAlreadyExistsException.class,
        DataIntegrityViolationException.class,
        UserAlreadyVerifiedException.class
    })
    public ProblemDetail handleConflict(RuntimeException ex, WebRequest request) {
        return build(ex, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException exception, WebRequest request) {
        return build(exception, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ProblemDetail handleForbiddenException(ForbiddenException exception, WebRequest request) {
        return build(exception, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(OTPNotVerifiedException.class)
    public ProblemDetail handleOtpNotVerifiedException(OTPNotVerifiedException exception, WebRequest webRequest) {
        return build(exception, webRequest, HttpStatus.LOCKED);
    }

    @ExceptionHandler({BadCredentialsException.class, InvalidTokenException.class})
    public ProblemDetail handleBadCredentialsException(RuntimeException exception, WebRequest webRequest) {
        return build(exception, webRequest, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MaximumShareLimitReachedException.class)
    public ProblemDetail handleMaximumShareLimitReachedException(
            MaximumShareLimitReachedException exception, WebRequest webRequest) {
        return build(exception, webRequest, HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, WebRequest request) {
        return build(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ProblemDetail build(Exception ex, WebRequest webRequest, HttpStatus status) {
        ApiErrorCode errorCode = errorCodeMapper.mapException(ex);

        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(status.getReasonPhrase());

        problem.setDetail(sanitizeMessage(ex.getMessage(), errorCode));

        problem.setProperty("timeStamp", Instant.now());
        problem.setProperty("errorCode", errorCode.getCode());
        return problem;
    }

    private String sanitizeMessage(String message, ApiErrorCode errorCode) {
        if (message == null || message.isBlank()) {
            return errorCode.getDefaultMessage();
        }
        // Remove email addresses
        String sanitized = EMAIL_PATTERN.matcher(message).replaceAll("[email]");

        // Remove potential UUIDs (resource IDs)
        sanitized = UUID_PATTERN.matcher(sanitized).replaceAll("[uuid]");

        // Remove potential numeric IDs
        sanitized = NUMERIC_ID_PATTERN.matcher(sanitized).replaceAll("[id]");
        return sanitized;
    }
}

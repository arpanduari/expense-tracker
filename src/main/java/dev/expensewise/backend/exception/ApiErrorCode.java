package dev.expensewise.backend.exception;

/**
 * @author arpan
 * @since 2/2/26
 */
public enum ApiErrorCode {
    // Authentication & Authorization
    INVALID_CREDENTIALS("AUTH001", "Invalid credentials provided"),
    INVALID_TOKEN("AUTH002", "Invalid or expired token"),
    ACCESS_DENIED("AUTH003", "Access denied"),
    FORBIDDEN("AUTH004", "Forbidden resource"),
    OTP_NOT_VERIFIED("AUTH005", "OTP verification required"),

    // User Management
    USERNAME_EXISTS("USER001", "Username already exists"),
    EMAIL_EXISTS("USER002", "Email already exists"),
    USER_ALREADY_VERIFIED("USER003", "User account already verified"),

    // Password
    PASSWORD_MISMATCH("PWD001", "Passwords do not match"),
    PASSWORD_POLICY_VIOLATION("PWD002", "Password does not meet security requirements"),
    PASSWORD_RESET_TOKEN_SENT("PWD003", "Password reset token already sent"),

    // Resource
    RESOURCE_NOT_FOUND("RES001", "Requested resource not found"),
    RESOURCE_EXPIRED("RES002", "Resource has expired"),

    // Validation
    VALIDATION_FAILED("VAL001", "Input validation failed"),
    CONSTRAINT_VIOLATION("VAL002", "Constraint violation"),
    METHOD_NOT_ALLOWED("VAL003", "HTTP method not allowed"),

    // Rate Limiting
    SHARE_LIMIT_EXCEEDED("LIMIT001", "Maximum share limit reached"),

    // System
    DATA_INTEGRITY_VIOLATION("SYS001", "Data integrity violation"),
    INTERNAL_ERROR("SYS999", "Internal server error");

    private final String code;
    private final String defaultMessage;

    ApiErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
}

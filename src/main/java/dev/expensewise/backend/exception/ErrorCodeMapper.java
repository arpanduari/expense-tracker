package dev.expensewise.backend.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author arpan
 * @since 2/2/26
 */
@Component
public class ErrorCodeMapper {

    private static final Map<Class<? extends Exception>, ApiErrorCode> EXCEPTION_MAP = new HashMap<>();

    static {
        // Authentication & Authorization
        EXCEPTION_MAP.put(BadCredentialsException.class, ApiErrorCode.INVALID_CREDENTIALS);
        EXCEPTION_MAP.put(InvalidTokenException.class, ApiErrorCode.INVALID_TOKEN);
        EXCEPTION_MAP.put(AccessDeniedException.class, ApiErrorCode.ACCESS_DENIED);
        EXCEPTION_MAP.put(ForbiddenException.class, ApiErrorCode.FORBIDDEN);
        EXCEPTION_MAP.put(OTPNotVerifiedException.class, ApiErrorCode.OTP_NOT_VERIFIED);

        // User Management
        EXCEPTION_MAP.put(UsernameAlreadyExistsException.class, ApiErrorCode.USERNAME_EXISTS);
        EXCEPTION_MAP.put(EmailAlreadyExistsException.class, ApiErrorCode.EMAIL_EXISTS);
        EXCEPTION_MAP.put(UserAlreadyVerifiedException.class, ApiErrorCode.USER_ALREADY_VERIFIED);

        // Password
        EXCEPTION_MAP.put(PasswordNotMatchingException.class, ApiErrorCode.PASSWORD_MISMATCH);
        EXCEPTION_MAP.put(PasswordPolicyViolationException.class, ApiErrorCode.PASSWORD_POLICY_VIOLATION);
        EXCEPTION_MAP.put(PasswordResetTokenAlreadySentException.class, ApiErrorCode.PASSWORD_RESET_TOKEN_SENT);

        // Resource
        EXCEPTION_MAP.put(ResourceNotFoundException.class, ApiErrorCode.RESOURCE_NOT_FOUND);
        EXCEPTION_MAP.put(ResourceExpiredException.class, ApiErrorCode.RESOURCE_EXPIRED);

        // Rate Limiting
        EXCEPTION_MAP.put(MaximumShareLimitReachedException.class, ApiErrorCode.SHARE_LIMIT_EXCEEDED);

        // System
        EXCEPTION_MAP.put(DataIntegrityViolationException.class, ApiErrorCode.DATA_INTEGRITY_VIOLATION);
    }

    public ApiErrorCode mapException(Exception ex) {
        return EXCEPTION_MAP.getOrDefault(ex.getClass(), ApiErrorCode.INTERNAL_ERROR);
    }
}

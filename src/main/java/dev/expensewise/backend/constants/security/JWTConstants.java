package dev.expensewise.backend.constants.security;

/**
 * @author arpan
 * @since 8/3/25
 */
public final class JWTConstants {
    private JWTConstants() {
    }

    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_SECRET = "JWT_SECRET";
    public static final String JWT_ISSUER = "Expense_Tracker";
    public static final String JWT_SUBJECT = "JWT_TOKEN";
    public static final String JWT_REFRESH_SUBJECT = "JWT_REFRESH_TOKEN";
}

package dev.arpan.expensetracker.constants;

/**
 * @author arpan
 * @since 8/3/25
 */
public interface JWTConstants {
    String JWT_HEADER = "Authorization";
    String JWT_SECRET = "JWT_SECRET";
    String JWT_ISSUER = "Expense_Tracker";
    String JWT_SUBJECT = "JWT_TOKEN";
    String JWT_REFRESH_SUBJECT = "JWT_REFRESH_TOKEN";
}

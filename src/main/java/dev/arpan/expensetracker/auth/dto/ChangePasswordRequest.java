package dev.arpan.expensetracker.auth.dto;


/**
 * @author arpan
 * @since 8/22/25
 */

public record ChangePasswordRequest(String oldPassword, String newPassword) {
}

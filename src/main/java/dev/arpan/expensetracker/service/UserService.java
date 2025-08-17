package dev.arpan.expensetracker.service;

import dev.arpan.expensetracker.dto.UserDto;

/**
 * @author arpan
 * @since 8/17/25
 */
public interface UserService {
    UserDto getUserByUsername(String username);
}

package dev.arpan.expensetracker.controller;

import dev.arpan.expensetracker.dto.UserDto;
import dev.arpan.expensetracker.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author arpan
 * @since 8/17/25
 */
@RestController
@RequestMapping("${api.base}${api.version}/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserDto> getUserDetailsAfterLogin(Authentication authentication) {
        UserDto userDto = userService.getUserByUsername(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(userDto);
    }

}

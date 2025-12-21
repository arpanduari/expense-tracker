package dev.arpan.expensetracker.common.filter;

import dev.arpan.expensetracker.auth.util.JWTUtil;
import dev.arpan.expensetracker.constants.application.ApplicationConstants;
import dev.arpan.expensetracker.constants.security.JWTConstants;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.user.User;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * @author arpan
 * @since 8/3/25
 */
@Component
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader(JWTConstants.JWT_HEADER);
        if (jwt != null) {
            try {
                Claims claims = jwtUtil.parseToken(jwt);
                String sub = claims.get("sub", String.class);
                if (!Objects.equals(sub, JWTConstants.JWT_SUBJECT)) {
                    throw new BadRequestException("Invalid accessToken");
                }
                String username = claims.get("username", String.class);
                Long userId = claims.get(ApplicationConstants.USER_ID, Long.class);
                if (jwtUtil.isTokenExpired(claims.getExpiration())) {
                    throw new BadRequestException("Invalid or Expired accessToken");
                }

                User user = User.builder()
                        .id(userId)
                        .username(username)
                        .build();

                CustomUserDetails customUserDetails = new CustomUserDetails(user);
                Authentication authentication = new UsernamePasswordAuthenticationToken(customUserDetails, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if (jwtUtil.shouldRefreshToken(claims.getExpiration())) {
                    String newToken = jwtUtil.generateAccessToken(username, userId);
                    response.setHeader(JWTConstants.JWT_HEADER, newToken);
                }
            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/api/v1/auth") &&
                !request.getRequestURI().equals("/api/v1/auth/change-password");
    }
}

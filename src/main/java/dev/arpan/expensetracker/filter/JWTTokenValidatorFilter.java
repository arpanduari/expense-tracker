package dev.arpan.expensetracker.filter;

import dev.arpan.expensetracker.constants.ApplicationConstants;
import dev.arpan.expensetracker.constants.JWTConstants;
import dev.arpan.expensetracker.entity.User;
import dev.arpan.expensetracker.security.CustomUserDetails;
import dev.arpan.expensetracker.utils.JWTUtil;
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
                String username = claims.get("username", String.class);
                Long userId = claims.get(ApplicationConstants.USER_ID, Long.class);
                if (jwtUtil.isTokenExpired(claims.getExpiration())) {
                    throw new BadRequestException("Invalid or Expired token");
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
        return request.getRequestURI().startsWith("/api/v1/auth");
    }
}

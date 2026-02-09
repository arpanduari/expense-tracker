package dev.expensewise.backend.common.filter;

import dev.expensewise.backend.config.security.JWTService;
import dev.expensewise.backend.config.security.TokenType;
import dev.expensewise.backend.constants.application.ApplicationConstants;
import dev.expensewise.backend.constants.security.JWTConstants;
import dev.expensewise.backend.config.security.CustomUserDetails;
import dev.expensewise.backend.user.User;
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
import java.util.Optional;

/**
 * @author arpan
 * @since 8/3/25
 */
@Component
@RequiredArgsConstructor
public class JWTTokenValidatorFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = request.getHeader(JWTConstants.JWT_HEADER);
        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try {
                Claims claims = jwtService.parseToken(jwt, TokenType.ACCESS);
                Authentication authentication = createAuthentication(claims);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception ex) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Unauthorized\"}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri.startsWith("/api/v1/auth") && !uri.equals("/api/v1/auth/change-password")
                || uri.startsWith("/api/v1/ledger/share/public/")
                || uri.equals("/api/v1/ledger/shared-entries")
                || uri.startsWith("/api-docs/")
                || uri.startsWith("/swagger-ui/")
                || uri.startsWith("/actuator/health")
                || uri.startsWith("/actuator/info")
                || uri.equals("/universal-link.html");
    }

    private Authentication createAuthentication(Claims claims) throws BadRequestException {
        String username = Optional.ofNullable(claims.get("username", String.class))
                .orElseThrow(() -> new BadRequestException("Invalid accessToken"));
        Long userId = Optional.ofNullable(claims.get(ApplicationConstants.USER_ID, Long.class))
                .orElseThrow(() -> new BadRequestException("Invalid accessToken"));

        if (jwtService.isTokenExpired(claims.getExpiration())) {
            throw new BadRequestException("Invalid or Expired accessToken");
        }

        User user = User.builder().id(userId).username(username).build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, Collections.emptyList());
    }
}

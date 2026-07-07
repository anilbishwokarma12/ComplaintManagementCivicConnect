package com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.jwt;

import com.ComplaintManagementCivicConnect.ComplaintManagementCivicConnect.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();
        String method = request.getMethod();

        log.info("🔍 Request: {} {}", method, path);

        // ✅ bypass auth endpoints
        if (path.startsWith("/api/auth/")) {
            log.info("⏭️ Skipping auth endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        // Allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(method)) {
            log.info("⏭️ Skipping OPTIONS request");
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        log.info("🔑 Authorization header: {}", authHeader != null ? "✅ Present" : "❌ Missing");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("⚠️ No Bearer token found");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            log.info("🔑 JWT: {}...", jwt.substring(0, Math.min(jwt.length(), 30)));

            final String userEmail = jwtUtil.extractUserName(jwt);
            log.info("👤 Extracted email: {}", userEmail);

            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(userEmail);

                log.info("👤 User authorities from DB: {}", userDetails.getAuthorities());

                if (jwtUtil.isTokenValid(jwt, userDetails)) {
                    log.info("✅ Token is valid for user: {}", userEmail);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("✅ Authentication set with authorities: {}", userDetails.getAuthorities());
                } else {
                    log.warn("❌ Token is INVALID for user: {}", userEmail);
                }
            } else {
                log.info("⏭️ Skipping: userEmail={}, authentication={}",
                        userEmail, authentication != null ? "✅ set" : "❌ null");
            }

        } catch (Exception e) {
            log.error("❌ JWT Error: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}
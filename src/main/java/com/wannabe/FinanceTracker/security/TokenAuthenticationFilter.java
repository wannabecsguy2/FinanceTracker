package com.wannabe.FinanceTracker.security;

import com.wannabe.FinanceTracker.utils.JWTUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    @Value("${app.base.url}")
    private String baseUrl;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private static final List<String> doNotFilterRoutes = new ArrayList<>(List.of("/auth", "/country/fetch-all", "/currency/fetch-all", "/webhook/twilio", "/transaction-method/fetch-all", "/transaction-tag/fetch-all"));

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            log.info("Filter request for URI: " + request.getRequestURI());
            String jwt = getJwtFromRequest(request);

            assert jwt != null;
            if (!jwt.isBlank()) {
                if (jwtUtils.validateJWT(jwt)) {
                    UUID userId = jwtUtils.getUserIdFromJWT(jwt);
                    UserPrincipal userPrincipal = customUserDetailsService.loadUserById(userId);
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    log.error("JWT is invalid");
                }
            }
        } catch (Exception e) {
            log.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        for (String route: doNotFilterRoutes) {
            if (request.getRequestURI().startsWith(baseUrl.concat(route))) {
                return true;
            }
        }
        return false;
    }

    private String getJwtFromRequest (HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (!bearerToken.isBlank() && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

package com.wannabe.FinanceTracker.utils;

import com.wannabe.FinanceTracker.security.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JWTUtils {

    @Value("${app.auth.jwtSecret}")
    private String jwtSecret;

    @Value("${app.auth.jwtValidityMsec}")
    private long jwtValidityMsec;

    public String createJWT(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return createJWT(userPrincipal);
    }

    public String createJWT(UserPrincipal userPrincipal) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtValidityMsec);

        return Jwts.builder()
                .subject(String.valueOf(userPrincipal.getId()))
                .issuedAt(new Date())
                .expiration(expiryDate)
                .claims(createJWTSubject(userPrincipal))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public UUID getUserIdFromJWT(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(jwt).getBody();
        return UUID.fromString(claims.getSubject());
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean validateJWT(String jwt) {
        try {
            Jwts.parser().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        }
        return false;
    }

    public Map<String, Object> createJWTSubject(UserPrincipal userPrincipal) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", userPrincipal.getId());
        claims.put("email", userPrincipal.getEmail());
        claims.put("username", userPrincipal.getUsername());

        return claims;
    }
}

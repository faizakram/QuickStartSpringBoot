package com.app.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
@Component
public class JwtUtil {
    private static final String SECRET_KEY = "q2iL9s2XYaYg2OADBfMN5ERYCOH263zfYX+xd7yW5+8="; // Must be 256-bit Base64 encoded

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY));
    }

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 min expiry
                .signWith(getSigningKey()) // No need to specify algorithm in 0.12.x
                .compact();
    }

    public String extractUsername(String token) {
        return getClaims(token).get("username", String.class);
    }

    public boolean validateToken(String token) {
        return extractUsername(token) != null && getClaims(token).getExpiration().after(new Date());
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // Secure verification
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

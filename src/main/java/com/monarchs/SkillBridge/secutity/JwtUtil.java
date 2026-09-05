package com.monarchs.SkillBridge.secutity;

import com.monarchs.SkillBridge.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    public JwtUtil(@Value("${jwt.signature}") String jwtSignature) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSignature.getBytes(StandardCharsets.UTF_8));
    }
    public String createJwtToken(User user){
        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("role",user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1200*1000))
                .signWith(secretKey)
                .compact();
    }
    public Claims parseClaims(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

package com.bugnbass.backend.config;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Service
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.accesstoken-expiration}")
    private long jwtExpiration;

    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    public String generateToken(String email, UserRole role) {
        return Jwts.builder()
                .setSubject(email) //set e-mail as identity
                .claim("role", role.name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public UserRole getRoleFromToken(String token) {
        String role = Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);

        return UserRole.valueOf(role);
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

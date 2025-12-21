package com.bugnbass.backend.config;

import com.bugnbass.backend.model.enums.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * Utility service for generating and validating JWT (JSON Web Tokens).
 * Provides methods to create tokens, extract user information, and validate tokens.
 */
@Service
public class JwtUtil {

    /** Secret key for signing JWTs, injected from application configuration. */
    @Value("${jwt.secret}")
    private String jwtSecret;

    /** Expiration time in milliseconds for access tokens. */
    @Value("${jwt.accesstoken-expiration}")
    private long jwtExpiration;

    /** Internal signing key generated from the secret. */
    private Key key;

    /**
     * Initializes the signing key after the bean is constructed.
     * Decodes the base64-encoded secret and creates an HMAC-SHA key.
     */
    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(jwtSecret);
        this.key = Keys.hmacShaKeyFor(bytes);
    }

    /**
     * Generates a JWT for the given email and role.
     *
     * @param email the email to set as the token's subject
     * @param role the user's role to include in the token claims
     * @return the signed JWT as a string
     */
    public String generateToken(String email, UserRole role) {
        return Jwts.builder()
            .setSubject(email) // set e-mail as identity
            .claim("role", role.name())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /**
     * Extracts the email (subject) from a JWT.
     *
     * @param token the JWT to parse
     * @return the email stored in the token
     */
    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(key).build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
    }

    /**
     * Extracts the user role from a JWT.
     *
     * @param token the JWT to parse
     * @return the {@link UserRole} stored in the token claims
     */
    public UserRole getRoleFromToken(String token) {
        String role = Jwts.parser().setSigningKey(key).build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);

        return UserRole.valueOf(role);
    }

    /**
     * Validates a JWT, checking its signature and expiration.
     *
     * @param token the JWT to validate
     * @return true if the token is valid, false otherwise
     */
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

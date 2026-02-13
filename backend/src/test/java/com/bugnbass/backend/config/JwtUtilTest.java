package com.bugnbass.backend.config;

import com.bugnbass.backend.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        byte[] secretBytes = "0123456789ABCDEF0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);

        setField(jwtUtil, "jwtSecret", base64Secret);
        setField(jwtUtil, "jwtExpiration", 60_000L);

        jwtUtil.init();
    }

    @Test
    void generateToken_thenExtractEmailAndRole_ok() {
        String token = jwtUtil.generateToken("max@test.com", UserRole.ROLE_USER);

        assertThat(token).isNotBlank();
        assertThat(jwtUtil.getEmailFromToken(token)).isEqualTo("max@test.com");
        assertThat(jwtUtil.getRoleFromToken(token)).isEqualTo(UserRole.ROLE_USER);
        assertThat(jwtUtil.validateJwtToken(token)).isTrue();
    }

    @Test
    void validateJwtToken_returnsFalse_forMalformedToken() {
        assertThat(jwtUtil.validateJwtToken("not-a-jwt")).isFalse();
    }

    @Test
    void validateJwtToken_returnsFalse_forTamperedToken() {
        String token = jwtUtil.generateToken("max@test.com", UserRole.ROLE_USER);

        String[] parts = token.split("\\.");
        assertThat(parts).hasSize(3);

        // break signature deterministically
        String tampered = parts[0] + "." + parts[1] + ".AAAAAAAAAAAAAAAAAAAAAA";

        assertThat(jwtUtil.validateJwtToken(tampered)).isFalse();
    }

    @Test
    void validateJwtToken_returnsFalse_forExpiredToken() {
        JwtUtil util = new JwtUtil();

        byte[] secretBytes = "0123456789ABCDEF0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);

        setField(util, "jwtSecret", base64Secret);
        setField(util, "jwtExpiration", -1_000L);
        util.init();

        String expiredToken = util.generateToken("max@test.com", UserRole.ROLE_USER);

        assertThat(util.validateJwtToken(expiredToken)).isFalse();
    }

    @Test
    void validateJwtToken_returnsFalse_forNullToken() {
        assertThat(jwtUtil.validateJwtToken(null)).isFalse();
    }

    @Test
    void getEmailFromToken_throws_forInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.getEmailFromToken("not-a-jwt"))
                .isInstanceOf(Exception.class);
    }

    @Test
    void getRoleFromToken_throws_forInvalidToken() {
        assertThatThrownBy(() -> jwtUtil.getRoleFromToken("not-a-jwt"))
                .isInstanceOf(Exception.class);
    }

    // ---------------- helpers ----------------

    private static void setField(Object target, String fieldName, Object value) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}

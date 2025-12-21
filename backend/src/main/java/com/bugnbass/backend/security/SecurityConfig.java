package com.bugnbass.backend.security;

import static org.springframework.security.config.Customizer.withDefaults;

import com.bugnbass.backend.config.CustUserDetailsService;
import com.bugnbass.backend.config.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * Spring Security configuration class.
 * Configures JWT authentication, password encoding, CORS, session management, and URL access rules.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtils;
    private final CustUserDetailsService custUserDetailsService;
    private final AuthEntryPoint unauthorizedHandler;

    /**
     * Constructs the SecurityConfig with required dependencies.
     *
     * @param custUserDetailsService the custom user details service for loading user information
     * @param unauthorizedHandler    handler for unauthorized access attempts
     * @param jwtUtils               utility class for generating and validating JWT tokens
     */
    public SecurityConfig(
            CustUserDetailsService custUserDetailsService,
            AuthEntryPoint unauthorizedHandler,
            JwtUtil jwtUtils
    ) {
        this.custUserDetailsService = custUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    }

    /**
     * JWT authentication filter bean.
     */
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, custUserDetailsService);
    }

    /**
     * Authentication manager bean using the application's user details service.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Password encoder bean using BCrypt hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures HTTP security including JWT, CORS, session management, and URL authorization.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // public API endpoints
                        .requestMatchers(
                                "/bugnbass/api/auth/**",
                                "/bugnbass/api/shop/**",
                                "/bugnbass/api/media/**",
                                "/bugnbass/api/swagger-ui/**",
                                "/bugnbass/api/v3/api-docs/**"
                        ).permitAll()
                        // admin endpoints require authentication
                        .requestMatchers("/bugnbass/api/admin/**").authenticated()
                        .anyRequest().permitAll()
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http.addFilterBefore(
                authenticationJwtTokenFilter(),
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
}

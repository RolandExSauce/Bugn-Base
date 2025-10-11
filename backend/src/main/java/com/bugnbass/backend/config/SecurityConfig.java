package com.bugnbass.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

//sources used: https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/form.html
//https://medium.com/@victoronu/implementing-jwt-authentication-in-a-simple-spring-boot-application-with-java-b3135dbdb17b

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtils;
    private final CustUserDetailsService custUserDetailsService;
    private final AuthEntryPoint unauthorizedHandler;

    public SecurityConfig(
            CustUserDetailsService custUserDetailsService,
            AuthEntryPoint unauthorizedHandler,
            JwtUtil jwtUtils
    ) {
        this.custUserDetailsService = custUserDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.jwtUtils = jwtUtils;
    };

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtils, custUserDetailsService);
    };

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    };

    //security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                //.csrf(AbstractHttpConfigurer::disable) // Disable CSRF
                //.cors(AbstractHttpConfigurer::disable) // Disable CORS
                .cors(withDefaults())
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers(
                                        "/bugnbass/auth/",
                                        "/bugnbass/test/all",
                                        "/swagger-ui/",
                                        "/v3/api-docs/"
                                ).permitAll()
                                .requestMatchers("/bugnbass/")
                                .authenticated()
                                .anyRequest().denyAll()
                )
                //disable basic auth and form login
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        // Add the JWT Token filter before the usernamePasswordAuthenticationFilter
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    };
};



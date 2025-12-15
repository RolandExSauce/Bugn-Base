package com.bugnbass.backend.security;
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
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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
                .csrf(AbstractHttpConfigurer::disable)
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
                                        "/bugnbass/auth/**",
                                        "/bugnbass/test/all",
                                        "/swagger-ui/**",
                                        "/bugnbass/api/shop/**",
                                        "/v3/api-docs/**",
                                        "/product_images/**",
                                        //should be protected:
                                        "/bugnbass/api/admin/**",
                                        "/bugnbass/api/orders/**"
                                ).permitAll()
//                                .requestMatchers("/bugnbass/api/admin/**").authenticated()
//                                .requestMatchers("/bugnbass/api/orders/**").authenticated()
                                //.requestMatchers("/bugnbass/**").authenticated()
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



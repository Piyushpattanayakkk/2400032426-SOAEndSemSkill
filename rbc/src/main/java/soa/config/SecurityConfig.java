package soa.config;

import java.util.Collection;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import soa.service.JWTService;

@Configuration
public class SecurityConfig {

    private final JWTService jwtService;

    public SecurityConfig(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for REST APIs
            .csrf(csrf -> csrf.disable())

            // JWT is stateless
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // LOGIN and REGISTER are PUBLIC
                .requestMatchers("/rbc/user/**")
                .permitAll()

                // GET BOOKS - USER, ADMIN, FACULTY, STUDENT
                .requestMatchers(
                    HttpMethod.GET,
                    "/rbc/books",
                    "/rbc/books/**"
                )
                .hasAnyRole(
                    "ADMIN",
                    "USER",
                    "FACULTY",
                    "STUDENT"
                )

                // ADD BOOK - ADMIN ONLY (USER gets 403 Forbidden)
                .requestMatchers(
                    HttpMethod.POST,
                    "/rbc/books",
                    "/rbc/books/**"
                )
                .hasRole("ADMIN")

                // UPDATE BOOK - ADMIN ONLY
                .requestMatchers(
                    HttpMethod.PUT,
                    "/rbc/books/**"
                )
                .hasRole("ADMIN")

                // DELETE BOOK - ADMIN ONLY
                .requestMatchers(
                    HttpMethod.DELETE,
                    "/rbc/books/**"
                )
                .hasRole("ADMIN")

                // USERS - ADMIN ONLY
                .requestMatchers("/rbc/users/**")
                .hasRole("ADMIN")

                // Everything else requires JWT
                .anyRequest()
                .authenticated()
            )

            // JWT authentication
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt ->
                    jwt.jwtAuthenticationConverter(
                        jwtAuthenticationConverter()
                    )
                )
            );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey key = jwtService.getKey();

        return NimbusJwtDecoder
                .withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS384)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter =
                new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            String role = jwt.getClaimAsString("role");

            if (role == null || role.isEmpty()) {
                return List.of();
            }

            Collection<GrantedAuthority> authorities =
                    List.of(
                        new SimpleGrantedAuthority(
                            "ROLE_" + role
                        )
                    );

            return authorities;
        });

        return converter;
    }
}
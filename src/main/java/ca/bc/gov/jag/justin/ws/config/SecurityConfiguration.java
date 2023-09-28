package ca.bc.gov.jag.justin.ws.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
class SecurityConfiguration {
    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(
                httpSecurityCsrfConfigurer -> {
                    httpSecurityCsrfConfigurer.disable();
                }).cors(
                cors -> {
                    cors.disable();
                });

        http.authorizeHttpRequests(requests -> requests
                .anyRequest().authenticated());

        http.oauth2ResourceServer((oauth2) -> oauth2.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthConverter)));

        http.sessionManagement(
                httpSecuritySessionManagementConfigurer -> {
                    httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS);
                });

        return http.build();
    }
}

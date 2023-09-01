package ca.bc.gov.jag.justin.ws.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanKeycloakConfiguration {
    // https://www.appsloveworld.com/springboot/100/13/unable-to-build-spring-based-project-for-authentication-using-keycloak
    @Bean
    public KeycloakSpringBootConfigResolver KeycloakConfigResolver() {
        return new KeycloakSpringBootConfigResolver();
    }
}

package ca.bc.gov.jag.justin.ws;

import ca.bc.gov.jag.justin.ws.config.KeycloakSecurityConfig;
import ca.bc.gov.jag.justin.ws.service.CourtlistDataExtractService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourtListKeycloakSecurityConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(KeycloakSecurityConfig.class);


    @Test
    public void testConfigure() {

        context.run(it -> {
            assertThat(it).hasSingleBean(SessionAuthenticationStrategy.class);
            assertThat(it).hasSingleBean(KeycloakConfigResolver.class);
        });
    }
}

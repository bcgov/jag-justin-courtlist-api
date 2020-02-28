package ca.bc.gov.jag.justin.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 *
 * Spring boot application main class
 *
 * @author sivakaruna
 *
 */
@SpringBootApplication
public class CourtlistDataExtractApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(CourtlistDataExtractApplication.class, args);
	}

}

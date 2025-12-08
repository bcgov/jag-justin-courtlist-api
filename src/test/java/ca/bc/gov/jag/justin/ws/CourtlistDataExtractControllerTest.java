package ca.bc.gov.jag.justin.ws;

import ca.bc.gov.jag.justin.objects.JustinCourtListDataType;
import ca.bc.gov.jag.justin.ws.api.CourtlistDataExtractController;
import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import ca.bc.gov.jag.justin.ws.service.CourtlistDataExtractService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.mockito.ArgumentMatchers.any;

class CourtlistDataExtractControllerTest {

	@Mock
	CourtlistDataExtractService service;

	@Mock
	private SecurityContext securityContextMock;

	@Mock
	private Authentication authenticationMock;

	@Mock
	private Jwt keycloakPrincipalMock;

	CourtlistDataExtractController sut;
	
	JustinCourtListDataType _response = new JustinCourtListDataType();

	@BeforeEach
	public void beforeEach() {

		MockitoAnnotations.openMocks(this);

		Mockito.when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
		Mockito.when(authenticationMock.getPrincipal()).thenReturn(keycloakPrincipalMock);

		SecurityContextHolder.setContext(securityContextMock);

        sut = new CourtlistDataExtractController(service);

	}

	@DisplayName("Success - CourtlistDataExtractController")
	@Test
	void testExtractData() throws CourtlistDataExtractException {

		Mockito.when(service.extractData(any(), any())).thenReturn(new ResponseEntity(_response,HttpStatus.OK));
		ResponseEntity<?> response = sut.extractData("startDate", "endDate");
		Assertions.assertTrue(_response.equals(response.getBody()));
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@DisplayName("Success - CourtlistDataExtractController")
	@Test
	void testGetData() throws CourtlistDataExtractException {

		Mockito.when(service.getData(any(), any())).thenReturn("SUCCESS");
		String response = sut.getData("startDate", "endDate");
		Assertions.assertEquals("SUCCESS", response);

	}

}

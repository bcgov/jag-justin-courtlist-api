package ca.bc.gov.jag.justin.ws;

import ca.bc.gov.jag.justin.ws.api.CourtlistDataExtractController;
import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import ca.bc.gov.jag.justin.ws.service.CourtlistDataExtractService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.bc.gov.jag.justin.objects.JustinCourtListDataType;

import static org.mockito.ArgumentMatchers.any;

/**
*
* Tests for court list data extract controller
*
* @author sivakaruna
*
*/
class CourtlistDataExtractControllerTest {

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Mock
	CourtlistDataExtractService service;

	@InjectMocks
	CourtlistDataExtractController controller = new CourtlistDataExtractController();
	
	JustinCourtListDataType _response =new JustinCourtListDataType();

	@DisplayName("Success - CourtlistDataExtractController")
	@Test
	void testExtractData() throws CourtlistDataExtractException {

		Mockito.when(service.extractData(any(), any())).thenReturn(new ResponseEntity(_response,HttpStatus.OK));
		ResponseEntity<?> response = controller.extractData("startDate", "endDate");
		Assertions.assertTrue(_response.equals(response.getBody()));
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

	}

	@DisplayName("Success - CourtlistDataExtractController")
	@Test
	void testGetData() throws CourtlistDataExtractException {

		Mockito.when(service.getData(any(), any())).thenReturn("SUCCESS");
		String response = controller.getData("startDate", "endDate");
		Assertions.assertEquals("SUCCESS", response);

	}

}

package ca.bc.gov.jag.justin.ws;

import ca.bc.gov.jag.justin.ws.error.CourtListControllerAdvisor;
import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class CourtListControllerAdvisorTest {

    @Mock
    WebClientResponseException clientResponseException;

    CourtListControllerAdvisor sut;

    @BeforeEach
    public void initMocks() {

        MockitoAnnotations.openMocks(this);

        sut = new CourtListControllerAdvisor();

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testCourtListException() {

        ResponseEntity<Object> result = sut.handleCourtListException(new CourtlistDataExtractException("Something went wrong"));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>Something went wrong</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testDataBufferException() {

        ResponseEntity<Object> result = sut.handleDataBufferException(new DataBufferLimitException("Something went wrong"));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>Requested data too large to retrieve</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }

    @Test
    @DisplayName("400: Assert bad request returned")
    public void testWebClientException() {

        Mockito.when(clientResponseException.getStatusText()).thenReturn("HELP");

        ResponseEntity<Object> result = sut.handleWebClientException(clientResponseException);

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>HELP</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }

    @Test
    @DisplayName("500: Assert bad request returned")
    public void testRunTimeException() {

        ResponseEntity<Object> result = sut.handleRunTimeException(new RuntimeException("Something went wrong"));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>Service unavailable</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }

    @Test
    @DisplayName("500: Assert bad request returned")
    public void testGeneralException() {

        ResponseEntity<Object> result = sut.handleException(new Exception("Something went wrong"));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>Unknown error occured</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }

    @Test
    @DisplayName("401: Assert unauthorized returned")
    public void testUnAuthException() {

        ResponseEntity<Object> result = sut.handleUnAuthorizedException(new AccessDeniedException("Something went wrong"));

        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        Assertions.assertEquals("<Error><ErrorMessage>Something went wrong</ErrorMessage><ErrorCode>-1</ErrorCode></Error>", result.getBody());

    }


}

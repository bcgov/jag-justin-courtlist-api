package ca.bc.gov.jag.justin.ws.error;

import ca.bc.gov.jag.justin.ws.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@ControllerAdvice
public class CourtListControllerAdvisor {

    Logger logger = LoggerFactory.getLogger(CourtListControllerAdvisor.class);

    @ExceptionHandler(CourtlistDataExtractException.class)
    public ResponseEntity<Object> handleCourtListException(CourtlistDataExtractException ex) {

        logger.error("CourtList Error", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, ex.getMessage(), Keys.ERROR_RESPONSE_CODE), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataBufferLimitException.class)
    public ResponseEntity<Object> handleDataBufferException(DataBufferLimitException ex) {

        logger.error("DataBuffer Error", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, Keys.BUFFER_LIMIT_EXCEEDED_ERROR, Keys.ERROR_RESPONSE_CODE), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<Object> handleWebClientException(WebClientResponseException ex) {

        logger.error("Ords Client Exception", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, String.format("Exception calling ords service: %s", ex.getStatusText()), Keys.ERROR_RESPONSE_CODE), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRunTimeException(RuntimeException ex) {

        logger.error("Runtime Error", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, Keys.SERVICE_UNAVAILABLE_ERROR, Keys.ERROR_RESPONSE_CODE), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {

        logger.error("Exception ", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, Keys.UNKOWN_ERROR, Keys.ERROR_RESPONSE_CODE), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(AccessDeniedException ex) {

        logger.error("Access Denied ", ex);

        return new ResponseEntity<>(String.format(Keys.ERROR_RESPONSE_XML, ex.getMessage(), Keys.ERROR_RESPONSE_CODE), HttpStatus.FORBIDDEN);

    }

}

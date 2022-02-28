package ca.bc.gov.jag.justin.ws.error;

/**
 * 
 * Custom exception for court list data extract api
 * 
 * @author sivakaruna
 *
 */
public class CourtlistDataExtractException extends Exception {

	private static final long serialVersionUID = 1L;

	public CourtlistDataExtractException(String message) {
		super(message);
	}

}

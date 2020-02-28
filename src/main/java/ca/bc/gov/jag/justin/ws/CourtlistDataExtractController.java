package ca.bc.gov.jag.justin.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * Rest controller to retrieve data from court list data extract service
 *
 * @author sivakaruna
 *
 */
@Controller
public class CourtlistDataExtractController {

	@Autowired
	CourtlistDataExtractService service;

	/**
	 * Court list data extract end point controller
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping(value = "/retrieveData", produces = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> extractData(@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {

		return service.extractData(startDate, endDate);

	}

}

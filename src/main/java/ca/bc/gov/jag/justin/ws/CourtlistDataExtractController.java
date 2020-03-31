package ca.bc.gov.jag.justin.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;



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
	

	Logger logger = LoggerFactory.getLogger(CourtlistDataExtractController.class);

	/**
	 * Court list data extract end point controller
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@GetMapping(value = "/retrieveData", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public ResponseEntity<?> extractData(@RequestParam(value="startDate",required = false) String startDate,
			@RequestParam(value="endDate",required = false) String endDate) {
        
		return service.extractData(startDate, endDate);

	}
	
	@GetMapping(value = "/getData", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE,MediaType.TEXT_HTML_VALUE})
	@ResponseBody
	public String extractData1(@RequestParam(value="startDate",required = false) String startDate,
			@RequestParam(value="endDate",required = false) String endDate) {
		
		//JustinCourtListDataType data = service.extractData1(startDate, endDate);
       //  htmlservice.parseData(startDate, endDate);
		return service.extractData1(startDate, endDate);

	}

}

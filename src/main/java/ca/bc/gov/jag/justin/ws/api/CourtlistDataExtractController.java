package ca.bc.gov.jag.justin.ws.api;

import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import ca.bc.gov.jag.justin.ws.service.CourtlistDataExtractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/*
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
	@PreAuthorize("hasRole('data-view')")
	public ResponseEntity<?> extractData(@RequestParam(value="startDate",required = false) String startDate,
			@RequestParam(value="endDate",required = false) String endDate) throws CourtlistDataExtractException {

		logger.info("Request for extract from: {}", ((Jwt)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getClaims().get("clientId"));

		return service.extractData(startDate, endDate);

	}
	
	@GetMapping(value = "/getData", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE,MediaType.TEXT_HTML_VALUE})
	@ResponseBody
	@PreAuthorize("hasRole('data-view')")
	public String getData(@RequestParam(value="startDate",required = false) String startDate,
						  @RequestParam(value="endDate",required = false) String endDate) throws CourtlistDataExtractException {

		logger.info("Request for extract from: {}", SecurityContextHolder.getContext().getAuthentication().getName());

		return service.getData(startDate, endDate);

	}

}

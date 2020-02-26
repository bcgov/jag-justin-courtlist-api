package ca.bc.gov.open;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class CourtlistController {

	@RequestMapping(value = "/retrieve", method = RequestMethod.GET)
	@ResponseBody
	public CourtlistResponse retrieveXml() {
		ResponseEntity<CourtlistResponse> responseEntity = new RestTemplate()
				.getForEntity("http://jsonplaceholder.typicode.com/posts/1", CourtlistResponse.class);
		return responseEntity.getBody();
	}

}

package ca.bc.gov.open;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourtlistResponse {
	private String body;

	public CourtlistResponse() {
	}

	public CourtlistResponse(String body) {
		super();
		this.body = body;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	@Override
	public String toString() {
		return "RetrieveXmlResponse [body=" + body + "]";
	}

}

package ca.bc.gov.jag.justin.ws;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 * Properties class to hold the application properties
 *
 * @author sivakaruna
 *
 */
@ConfigurationProperties(prefix = "courtlist")
public class CourtlistDataExtractProperties {

	// Court list data extract base url
	private String baseUrl;
	
	// Court list data extract authentication user name
	private String username;
	
	// Court list data extract authentication password
	private String password;
	
	// Court list data extract service uri with params
	private String dataExtractUri;
	
	// In memory buffer size to handle large volumes of data
	private int dataBufferSize;

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDataExtractUri() {
		return dataExtractUri;
	}

	public void setDataExtractUri(String dataExtractUri) {
		this.dataExtractUri = dataExtractUri;
	}

	public int getDataBufferSize() {
		return dataBufferSize;
	}

	public void setDataBufferSize(int dataBufferSize) {
		this.dataBufferSize = dataBufferSize;
	}

}

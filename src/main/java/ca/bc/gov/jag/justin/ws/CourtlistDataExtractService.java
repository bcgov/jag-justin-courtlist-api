package ca.bc.gov.jag.justin.ws;

import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Mono;

/**
 *
 * Service class to retrieve data from court list data extract service
 *
 * @author sivakaruna
 *
 */
@Service
@Configuration
@EnableConfigurationProperties(CourtlistDataExtractProperties.class)
public class CourtlistDataExtractService {

	// Date pattern for request parameters
	private static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-[A-Z]{3}-\\d{4}$");

	// Error response and error messages
	private static final String ERROR_RESPONSE_XML = "<Error><ErrorMessage>%s</ErrorMessage><ErrorCode>%s</ErrorCode></Error>";

	private static final String MISSING_PARAMS_ERROR = "Missing required Request parameters";

	private static final String INVALID_PARAMS_ERROR = "Invalid parameter date format";

	private static final String UNAUTHORIZED_ERROR = "Request not authorized";

	private static final String BUFFER_LIMIT_EXCEEDED_ERROR = "Requested data teoo large to retrieve";

	private static final String SERVICE_UNAVAILABLE_ERROR = "Service unavailable";

	private static final String UNKOWN_ERROR = "Unknown error occured";

	private static final String ERROR_RESPONSE_CODE = "-1";

	@Autowired
	CourtlistDataExtractProperties properties;

	private WebClient webClient = null;

	/**
	 * Initialize web client
	 */
	@PostConstruct
	public void InitService() {

		// Authenticate using basic authentication
		// Configure max in memory size to handle massive amount of data retrieved
		this.webClient = WebClient.builder().baseUrl(properties.getBaseUrl())
				.defaultHeaders(header -> header.setBasicAuth(properties.getUsername(), properties.getPassword()))
				.exchangeStrategies(
						ExchangeStrategies.builder()
								.codecs(configurer -> configurer.defaultCodecs()
										.maxInMemorySize(properties.getDataBufferSize() * 1024 * 1024))
								.build())
				.build();

	}

	/**
	 * Call Court list data extract web service
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ResponseEntity<String> extractData(String startDate, String endDate) {

		try {
			validateParams(startDate, endDate);

			// Build request url with the input parameters
			String dataExtractUri = String.format(properties.getDataExtractUri(), startDate, endDate);

			Mono<String> responseBody = this.webClient.get().uri(dataExtractUri).retrieve().bodyToMono(String.class);

			return new ResponseEntity<String>(responseBody.block(), HttpStatus.OK);

		} catch (CourtlistDataExtractException e) {
			return new ResponseEntity<String>(String.format(ERROR_RESPONSE_XML, e.getMessage(), ERROR_RESPONSE_CODE),
					HttpStatus.BAD_REQUEST);
		} catch (DataBufferLimitException e) {
			return new ResponseEntity<String>(
					String.format(ERROR_RESPONSE_XML, BUFFER_LIMIT_EXCEEDED_ERROR, ERROR_RESPONSE_CODE),
					HttpStatus.INSUFFICIENT_STORAGE);
		} catch (WebClientResponseException e) {
			return new ResponseEntity<String>(
					String.format(ERROR_RESPONSE_XML, UNAUTHORIZED_ERROR, ERROR_RESPONSE_CODE),
					HttpStatus.UNAUTHORIZED);
		} catch (RuntimeException e) {
			return new ResponseEntity<String>(
					String.format(ERROR_RESPONSE_XML, SERVICE_UNAVAILABLE_ERROR, ERROR_RESPONSE_CODE),
					HttpStatus.SERVICE_UNAVAILABLE);
		} catch (Exception e) {
			return new ResponseEntity<String>(String.format(ERROR_RESPONSE_XML, UNKOWN_ERROR, ERROR_RESPONSE_CODE),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	/**
	 * Validate input parameters
	 * 
	 * @param startDate
	 * @param endDate
	 * @throws CourtlistDataExtractException
	 */
	public void validateParams(String startDate, String endDate) throws CourtlistDataExtractException {
		if (null == startDate || null == endDate) {
			throw new CourtlistDataExtractException(MISSING_PARAMS_ERROR);
		}
		if (!DATE_PATTERN.matcher(startDate).matches() || !DATE_PATTERN.matcher(endDate).matches()) {
			throw new CourtlistDataExtractException(INVALID_PARAMS_ERROR);
		}
	}

}

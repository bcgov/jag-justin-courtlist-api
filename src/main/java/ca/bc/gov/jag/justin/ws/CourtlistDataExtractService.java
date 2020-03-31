package ca.bc.gov.jag.justin.ws;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.jag.justin.objects.JustinCourtListDataType;
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
	public static final String ERROR_RESPONSE_XML = "<Error><ErrorMessage>%s</ErrorMessage><ErrorCode>%s</ErrorCode></Error>";

	public static final String MISSING_PARAMS_ERROR = "Missing required request parameters";

	public static final String INVALID_PARAMS_ERROR = "Invalid parameter date format";

	public static final String UNAUTHORIZED_ERROR = "Request not authorized";

	public static final String BUFFER_LIMIT_EXCEEDED_ERROR = "Requested data too large to retrieve";

	public static final String SERVICE_UNAVAILABLE_ERROR = "Service unavailable";

	public static final String UNKOWN_ERROR = "Unknown error occured";

	public static final String ERROR_RESPONSE_CODE = "-1";
	
	public static final String TRANSFORM_Error = "Failed to perform transformation";

	@Autowired
	CourtlistDataExtractProperties properties;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	private ResourceLoader resourceLoader;

	private WebClient webClient = null;

	Logger logger = LoggerFactory.getLogger(CourtlistDataExtractService.class);

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
	public ResponseEntity<?> extractData(String startDate, String endDate) {

		try {
			validateParams(startDate, endDate);

			// Build request url with the input parameters
			String dataExtractUri = String.format(properties.getDataExtractUri(), startDate, endDate);

			Mono<JustinCourtListDataType> responseBody = this.webClient.get().uri(dataExtractUri).retrieve()
					.bodyToMono(JustinCourtListDataType.class);
			// logger.debug(" The extractData .. -> ", responseBody.block());
			return new ResponseEntity<JustinCourtListDataType>(responseBody.block(), HttpStatus.OK);

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

	public String extractData1(String startDate, String endDate) {

		try {
			validateParams(startDate, endDate);

			// Build request url with the input parameters
			String dataExtractUri = String.format(properties.getDataExtractUri(), startDate, endDate);

			Mono<String> responseBody = this.webClient.get().uri(dataExtractUri).retrieve().bodyToMono(String.class);
			// convert the response to html 
			return transformToHtml(responseBody.block());

		} catch (CourtlistDataExtractException e) {
			return String.format(ERROR_RESPONSE_XML, e.getMessage(), ERROR_RESPONSE_CODE);

		} catch (DataBufferLimitException e) {
			return  String.format(ERROR_RESPONSE_XML, BUFFER_LIMIT_EXCEEDED_ERROR, ERROR_RESPONSE_CODE);

		} catch (WebClientResponseException e) {

		} catch (RuntimeException e) {
			return String.format(ERROR_RESPONSE_XML, SERVICE_UNAVAILABLE_ERROR, ERROR_RESPONSE_CODE);

		} catch (Exception e) {
			return String.format(ERROR_RESPONSE_XML, UNKOWN_ERROR, ERROR_RESPONSE_CODE);
		}
		return null;

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

	private String transformToHtml(String response)
			throws ParserConfigurationException, TransformerConfigurationException,TransformerException,SAXException, IOException {
		try {
			logger.info("Performing transformation.. ");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(response)));
			// Get the XSLT file
			File xsl = new ClassPathResource("courtlist.xslt").getFile();

			TransformerFactory transfomerFactory = TransformerFactory.newInstance();
			// Obtain the XSLT transformer

			StreamSource style = new StreamSource(xsl);
			Transformer transformer = transfomerFactory.newTransformer(style);

			DOMSource source = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);
			transformer.transform(source, result);
			String strResult = writer.toString();
			return strResult;
		}

		catch (ParserConfigurationException e) {
			//throw e;
			logger.error("Error occured while parsing" + e.getMessage());
			 return "Error occured while parsing " + e.getMessage() ;
		} catch (TransformerException e) {
			//throw e;
			return "Error TransformerException occured " + e.getMessage();
		} catch (IOException e) {
			//throw e;
			logger.error("Error IO Exception occured");
			return "Error IO Exception occured " + e.getMessage();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			//throw e;
			logger.error("Error : SAXException occured");
			return "Error : SAXException occured " + e.getMessage();
		}
		catch (Exception e) {
			logger.error("Error occured in transformToHtml");
			return "Error occured in transformToHtml " + e.getMessage();
			//throw e;
		}

	}
}

package ca.bc.gov.jag.justin.ws.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import ca.bc.gov.jag.justin.ws.Keys;
import ca.bc.gov.jag.justin.ws.config.CourtlistDataExtractProperties;
import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
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



	@Autowired
	CourtlistDataExtractProperties properties;

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
	public ResponseEntity<?> extractData(String startDate, String endDate) throws CourtlistDataExtractException {

		logger.info("Try extract data");

		validateParams(startDate, endDate);

		// Build request url with the input parameters
		String dataExtractUri = String.format(properties.getDataExtractUri(), startDate, endDate);

		Mono<JustinCourtListDataType> responseBody = this.webClient.get().uri(dataExtractUri).retrieve()
				.bodyToMono(JustinCourtListDataType.class);

		return new ResponseEntity<JustinCourtListDataType>(responseBody.block(), HttpStatus.OK);

	}

	/**
	 * Currently unsure of the purpose of this service
	 * @param startDate request start
	 * @param endDate request end
	 * @return raw data
	 */
	public String getData(String startDate, String endDate) throws CourtlistDataExtractException {

		validateParams(startDate, endDate);

		// Build request url with the input parameters
		String dataExtractUri = String.format(properties.getDataExtractUri(), startDate, endDate);

		Mono<String> responseBody = this.webClient.get().uri(dataExtractUri).retrieve().bodyToMono(String.class);
		// convert the response to html
		return transformToHtml(responseBody.block());

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
			throw new CourtlistDataExtractException(Keys.MISSING_PARAMS_ERROR);
		}
		if (!Keys.DATE_PATTERN.matcher(startDate).matches() || !Keys.DATE_PATTERN.matcher(endDate).matches()) {
			throw new CourtlistDataExtractException(Keys.INVALID_PARAMS_ERROR);
		}
	}

	private String transformToHtml(String response) {

		String result = null;

		try {

			logger.info("Performing transformation.. ");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new InputSource(new StringReader(response)));
			// Get the XSLT file
			InputStream xsl = new ClassPathResource("courtlist.xslt").getInputStream();

			TransformerFactory transfomerFactory = TransformerFactory.newInstance();
			// Obtain the XSLT transformer

			StreamSource style = new StreamSource(xsl);
			Transformer transformer = transfomerFactory.newTransformer(style);

			DOMSource source = new DOMSource(doc);
			StringWriter writer = new StringWriter();
			StreamResult streamResult = new StreamResult(writer);
			transformer.transform(source, streamResult);
			result = writer.toString();

		}

		catch (ParserConfigurationException e) {
			logger.error("Error occurred while parsing", e);
			result = String.format("Error occurred while parsing %s", e.getMessage()) ;
		} catch (TransformerException e) {
			logger.error("Error TransformerException occurred", e);
			result = String.format("Error TransformerException occurred %s", e.getMessage());
		} catch (IOException e) {
			logger.error("Error IO Exception occurred", e);
			result = String.format("Error IO Exception occurred %s", e.getMessage());
		} catch (SAXException e) {
			logger.error("Error : SAXException occurred", e);
			result = String.format("Error : SAXException occurred %s", e.getMessage());
		} catch (Exception e) {
			logger.error("Error occured in transformToHtml", e);
			result = String.format("Error occurred in transformToHtml %s", e.getMessage());
		}

		return result;

	}
}

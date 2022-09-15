package ca.bc.gov.jag.justin.ws;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import ca.bc.gov.jag.justin.ws.config.CourtlistDataExtractProperties;
import ca.bc.gov.jag.justin.ws.error.CourtlistDataExtractException;
import ca.bc.gov.jag.justin.ws.service.CourtlistDataExtractService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.jag.justin.objects.*;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import static org.mockito.ArgumentMatchers.any;

/**
*
* Tests for court list data extract service
*
* @author sivakaruna
*
*/
class CourtlistDataExtractServiceTest {

	public static MockWebServer mockBackEnd;
	@InjectMocks
	@Spy
	CourtlistDataExtractService service;

	@Mock
	CourtlistDataExtractProperties properties;
	@Mock
	ObjectMapper objectMapper;
	@InjectMocks
	JustinCourtListDataType _response = new JustinCourtListDataType();
	private static final Logger logger = LoggerFactory.getLogger(CourtlistDataExtractServiceTest.class);

	@BeforeAll
	static void setUp() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@BeforeEach
	void initialize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

		MockitoAnnotations.openMocks(this);
		String baseUrl = String.format("http://localhost:%s", mockBackEnd.getPort());
		Mockito.when(properties.getBaseUrl()).thenReturn(baseUrl);
		Mockito.when(properties.getUsername()).thenReturn("username");
		Mockito.when(properties.getPassword()).thenReturn("password");
		Mockito.when(properties.getDataExtractUri()).thenReturn("uri");
		Mockito.when(properties.getDataBufferSize()).thenReturn(1);

		Method postConstruct = CourtlistDataExtractService.class.getDeclaredMethod("InitService");
		postConstruct.setAccessible(true);
		postConstruct.invoke(service);

	}

	@DisplayName("Success - data extract call")
	@Test
	public void testSuccess() throws JsonProcessingException, CourtlistDataExtractException {

		String response = successResponseObject();
		Mockito.when(objectMapper.writeValueAsString(any())).thenReturn(response);
		MockResponse mockResponse = new MockResponse();
		mockResponse.setBody(response);
		mockResponse.addHeader("content-type: application/xml;");
		mockResponse.setResponseCode(200);
		mockBackEnd.enqueue(mockResponse);
		ResponseEntity<?> res = service.extractData("01-JAN-2020", "02-JAN-2020");
		Assertions.assertEquals(HttpStatus.OK, res.getStatusCode());
		Assertions.assertEquals(response, res.getBody());

	}
	
	@DisplayName("Success - data extract call")
	@Test
	public void testHTMLSuccess() throws JsonProcessingException, CourtlistDataExtractException {

		String response = successResponseObject();
		Mockito.when(objectMapper.writeValueAsString(any())).thenReturn(response);
		MockResponse mockResponse = new MockResponse();
		mockResponse.setBody(response);
		mockResponse.addHeader("content-type: application/xml;");
		mockResponse.setResponseCode(200);
		mockBackEnd.enqueue(mockResponse);
		String res = service.getData("01-JAN-2020", "02-JAN-2020");
		Assertions.assertNotNull(res);

	}

	@DisplayName("Missing param - data extract call")
	@Test
	public void testMissingParams() throws CourtlistDataExtractException {

		String response = "<Error><ErrorMessage>" + Keys.MISSING_PARAMS_ERROR
				+ "</ErrorMessage><ErrorCode>-1</ErrorCode></Error>";
		Assertions.assertThrows(CourtlistDataExtractException.class, () -> service.extractData(null, "02-JAN-2020"));

	}

	@DisplayName("Invalid param - data extract call")
	@Test
	public void testInvalidParams() throws CourtlistDataExtractException {

		String response = "<Error><ErrorMessage>" + Keys.INVALID_PARAMS_ERROR
				+ "</ErrorMessage><ErrorCode>-1</ErrorCode></Error>";

		Assertions.assertThrows(CourtlistDataExtractException.class, () -> service.extractData("Jan 01 2020", "02-JAN-2020"));

	}

	@DisplayName("Fail - data extract call")
	@Test
	public void testHTMLFail() throws JsonProcessingException, CourtlistDataExtractException {

		String response = successResponseObject();
		Mockito.when(objectMapper.writeValueAsString(any())).thenReturn(response);
		MockResponse mockResponse = new MockResponse();
		mockResponse.setBody("{}");
		mockResponse.addHeader("content-type: application/json;");
		mockResponse.setResponseCode(200);
		mockBackEnd.enqueue(mockResponse);
		String res = service.getData("01-JAN-2020", "02-JAN-2020");
		Assertions.assertEquals("Error occurred in transformToHtml Content is not allowed in prolog.", res);

	}
	
	private String successResponseObject() {
		JustinCourtListDataType jcd = new JustinCourtListDataType();
		
		PlmsCourtListType plms = new PlmsCourtListType();
		
		plms.setType("Provincial");
		
		jcd.setPlmsCourtList(plms);
		jcd.setStartDate("01-JAN-2020");
		jcd.setEndDate("02-JAN-2020");
		jcd.setExtract(new Date().toString());
		return jaxbObjectToXML(jcd);

	}
	
	public static String jaxbObjectToXML(Object obj) {
        String xmlString = "";
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller m = context.createMarshaller();

            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            
            QName qName = new QName("ca.bc.gov.jag.justin.objects", "JustinCourtListDataType");
            JAXBElement<JustinCourtListDataType> objBuild = new JAXBElement<JustinCourtListDataType>(qName, JustinCourtListDataType.class, (JustinCourtListDataType) obj);

            StringWriter sw = new StringWriter();
            m.marshal(objBuild, sw);
            xmlString = sw.toString();

        } catch (JAXBException e) {
            logger.error("Failed to marshall string");
        }

        return xmlString;
    }
	

}

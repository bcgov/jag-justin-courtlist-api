package ca.bc.gov.jag.justin.ws;

import ca.bc.gov.jag.justin.ws.config.CourtlistDataExtractProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CourtlistDataExtractPropertiesTest {

    private static final String testData = "TEST";


    @Test
    public void testProperties() {

        CourtlistDataExtractProperties sut = new CourtlistDataExtractProperties();

        sut.setBaseUrl(testData);
        sut.setDataBufferSize(1);
        sut.setDataExtractUri(testData);
        sut.setPassword(testData);
        sut.setUsername(testData);

        Assertions.assertEquals(testData, sut.getBaseUrl());
        Assertions.assertEquals(testData, sut.getDataExtractUri());
        Assertions.assertEquals(testData, sut.getPassword());
        Assertions.assertEquals(testData, sut.getUsername());
        Assertions.assertEquals(1, sut.getDataBufferSize());

    }

}

package ca.bc.gov.jag.justin.ws;

import java.util.regex.Pattern;

public class Keys {

    private Keys() {}

    // Date pattern for request parameters
    public static final Pattern DATE_PATTERN = Pattern.compile("^\\d{2}-[A-Z]{3}-\\d{4}$");

    // Error response and error messages
    public static final String ERROR_RESPONSE_XML = "<Error><ErrorMessage>%s</ErrorMessage><ErrorCode>%s</ErrorCode></Error>";

    public static final String MISSING_PARAMS_ERROR = "Missing required request parameters";

    public static final String INVALID_PARAMS_ERROR = "Invalid parameter date format";

    public static final String UNAUTHORIZED_ERROR = "Request not authorized";

    public static final String BUFFER_LIMIT_EXCEEDED_ERROR = "Requested data too large to retrieve";

    public static final String SERVICE_UNAVAILABLE_ERROR = "Service unavailable";

    public static final String UNKOWN_ERROR = "Unknown error occured";

    public static final String ERROR_RESPONSE_CODE = "-1";

}

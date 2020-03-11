package watertank.services.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import watertank.services.LoggingService;
import watertank.utils.LoggerUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoggingServiceImpl implements LoggingService {

    private static final ObjectMapper mapper = new ObjectMapper();
    private ObjectNode requestRootNode;
    private ObjectNode responseRootNode;

    public LoggingServiceImpl() {
        this.requestRootNode = mapper.createObjectNode();
        this.responseRootNode = mapper.createObjectNode();
    }

    @Override
    public void log(final ContentCachingRequestWrapper requestWrapper,
                    final ContentCachingResponseWrapper responseWrapper) throws IOException {
        String requestToLog = "RECV: " + getRequestData(requestWrapper).toString();
        String responseToLog = "SENT: " + getResponseData(responseWrapper).toString();

        if(responseWrapper.getStatus() < 400) {
            LoggerUtil.info(this.getClass(), requestToLog);
            LoggerUtil.info(this.getClass(), responseToLog);
        } else {
            LoggerUtil.error(this.getClass(), requestToLog);
            LoggerUtil.error(this.getClass(), responseToLog + "\n");
        }

        cleanObjectNode(requestRootNode);
        cleanObjectNode(responseRootNode);
    }

    private ObjectNode getRequestData(ContentCachingRequestWrapper requestWrapper) throws IOException {
        String httpMethod = requestWrapper.getMethod();
        JsonNode requestBody = mapper.readTree(requestWrapper.getContentAsByteArray());

        requestRootNode.put("uri", requestWrapper.getRequestURI());
        requestRootNode.put("clientIp", requestWrapper.getRemoteAddr());
        requestRootNode.put("method", httpMethod);
        requestRootNode.set("requestHeaders", mapper.valueToTree(getRequestHeaders(requestWrapper)));
        requestRootNode.set("requestUrlParams", mapper.valueToTree(requestWrapper.getParameterMap()));
        requestRootNode.set("request", requestBody);

        return requestRootNode;
    }

    private ObjectNode getResponseData(ContentCachingResponseWrapper responseWrapper) throws IOException {
        JsonNode responseBody = mapper.readTree(responseWrapper.getContentAsByteArray());

        responseRootNode.put("status", responseWrapper.getStatus());
        responseRootNode.set("responseHeaders", mapper.valueToTree(getResponseHeaders(responseWrapper)));
        responseRootNode.set("responseBody", responseBody);

        responseWrapper.copyBodyToResponse();

        return responseRootNode;
    }

    private Map<String, Object> getRequestHeaders(ContentCachingRequestWrapper requestWrapper) {
        Map<String, Object> headers = new HashMap<>();
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, requestWrapper.getHeader(headerName));
        }
        return headers;
    }

    private Map<String, Object> getResponseHeaders(ContentCachingResponseWrapper responseWrapper) {
        Map<String, Object> headers = new HashMap<>();
        Collection<String> headerNames = responseWrapper.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, responseWrapper.getHeader(headerName));
        }
        return headers;
    }

    private void cleanObjectNode(ObjectNode node) {
        node.removeAll();
    }
}


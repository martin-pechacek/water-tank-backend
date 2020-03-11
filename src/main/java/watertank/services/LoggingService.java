package watertank.services;

import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

public interface LoggingService {

    void log(ContentCachingRequestWrapper httpServletRequest, ContentCachingResponseWrapper responseWrapper) throws IOException;
}

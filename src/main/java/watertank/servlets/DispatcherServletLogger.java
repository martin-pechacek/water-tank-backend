package watertank.servlets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import watertank.services.LoggingServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServletLogger extends DispatcherServlet {

    @Autowired
    LoggingServiceImpl loggingService;

    @Override
    protected void doDispatch(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            super.doDispatch(requestWrapper, responseWrapper);
        } finally {
            loggingService.log(requestWrapper, responseWrapper);
        }
    }
}

package watertank.validations;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import watertank.utils.ErrorUtil;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception ex){
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        ErrorUtil.getCustomExceptionPayload(statusCode,ex);

        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(statusCode, ex),
                                    null,
                                    statusCode);
    }


    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatus status,
                                                                          final WebRequest request) {
        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(status, ex),
                                    headers,
                                    status);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        final HttpHeaders headers,
                                                        final HttpStatus status,
                                                        final WebRequest request) {

        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(status, ex),
                                    headers,
                                    status);
    }
}
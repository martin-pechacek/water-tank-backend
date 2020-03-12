package watertank.validations;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import watertank.utils.ErrorUtil;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Custom error response for general exception
     *
     * @param ex recived exception
     * @return ResponseEntity<Object> with custom error
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(final Exception ex){
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        ErrorUtil.getCustomExceptionPayload(statusCode,ex);

        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(statusCode, ex),
                                    null,
                                    statusCode);
    }

    /**
     * Custom error response for MethodArgumentNotValid exception
     *
     * @param ex received exception
     * @param headers received headers
     * @param status received status code
     * @param request received request
     * @return ResponseEntity<Object> with custom error
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        ErrorUtil.getCustomExceptionPayload(statusCode,ex);

        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(statusCode, ex),
                null,
                statusCode);
    }

    /**
     * Custom error response for MissingServletRequestParameter exception
     *
     * @param ex received exception
     * @param headers received headers
     * @param status received status code
     * @param request received request
     * @return ResponseEntity<Object> with custom error
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatus status,
                                                                          final WebRequest request) {
            return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(status, ex),
                                        headers,
                                        status);
        }

    /**
     * Custom error response for TypeMismatch exception
     *
     * @param ex received exception
     * @param headers received headers
     * @param status received status code
     * @param request received request
     * @return ResponseEntity<Object> with custom error
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        final HttpHeaders headers,
                                                        final HttpStatus status,
                                                        final WebRequest request) {

        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(status, ex),
                                    headers,
                                    status);
    }

    /**
     *  Custom error response for HttpMessageNotReadable exception
     *
     * @param ex received exception
     * @param headers received headers
     * @param status received status code
     * @param request received request
     * @return ResponseEntity<Object> with custom error
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return new ResponseEntity<>(ErrorUtil.getCustomExceptionPayload(status, ex),
                                    headers,
                                    status);
    }
}
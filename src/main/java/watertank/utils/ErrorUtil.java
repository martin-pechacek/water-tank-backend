package watertank.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

public class ErrorUtil {

    public static Map<String,Object> getCustomExceptionPayload(final HttpStatus status, final Exception exception){
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", DateUtil.getIsoDatetimeStamp());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("messages", getErrorMessages(exception));

        return body;
    }

    private static List<String> getErrorMessages(Exception ex){
        String exceptionType = ex.getClass().getSimpleName();
        List<String> errorMessages = new ArrayList<>();

        switch(exceptionType){
            case "MethodArgumentTypeMismatchException":
                errorMessages.add(ex.getMessage());
                break;
            case "MissingServletRequestParameterException":
                errorMessages.add(ex.getMessage());
                break;
            case "HttpMessageNotReadableException":
                errorMessages.add(((HttpMessageNotReadableException) ex).getMessage());
                break;
            case "MethodArgumentNotValidException":
                Iterator<FieldError> fieldErrorsIterator = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldErrors().iterator();

                while(fieldErrorsIterator.hasNext()) {
                    FieldError fieldError = fieldErrorsIterator.next();

                    String errorMessage = fieldError.getDefaultMessage();

                    errorMessages.add(errorMessage);
                }

                break;
            case "ConstraintViolationException":
                Throwable resultCause = ex;

                Iterator<ConstraintViolation<?>> constraintViolationsIterator = ((ConstraintViolationException) resultCause)
                                                                                    .getConstraintViolations()
                                                                                    .iterator();

                while(constraintViolationsIterator.hasNext()){
                    ConstraintViolation<?> constraintViolation = constraintViolationsIterator.next();

                    String error = constraintViolation.getPropertyPath() +
                                   " " +
                                   constraintViolation.getMessage();

                    errorMessages.add(error);
                }
                break;
            default:
                return errorMessages;
        }

        return errorMessages;
    }
}
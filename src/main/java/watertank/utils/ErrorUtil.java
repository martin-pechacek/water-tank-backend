package watertank.utils;

import org.springframework.http.HttpStatus;

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

    private static List<String> getErrorMessages(final Exception ex){
        String exceptionType = ex.getClass().getSimpleName();
        List<String> errorMessages = new ArrayList<>();

        switch(exceptionType){
            case "MethodArgumentTypeMismatchException":
                errorMessages.add(ex.getMessage());
                break;
            case "MissingServletRequestParameterException":
                errorMessages.add(ex.getMessage());
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
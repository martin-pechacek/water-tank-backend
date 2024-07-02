package watertank.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import watertank.dtos.ErrorDTO;
import watertank.exceptions.MeasurementException;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MeasurementException.class})
    protected ErrorDTO notFoundException(Exception exception) {
        return new ErrorDTO(
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                Set.of(new ObjectError(MeasurementException.class.getName(), exception.getMessage())));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ErrorDTO handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Set<ObjectError> errors = new HashSet<>(ex.getBindingResult().getAllErrors());

        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    protected ErrorDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {

        String errorMessage = "Parameter '" + ex.getName() + "' must be " + ex.getRequiredType().getSimpleName() + " type";

        return new ErrorDTO(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                Set.of(new ObjectError(NumberFormatException.class.getName(), errorMessage)));
    }
}

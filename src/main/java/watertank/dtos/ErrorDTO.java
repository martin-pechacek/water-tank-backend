package watertank.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.validation.ObjectError;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Setter
@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

    private int status;

    private String error;

    private Set<String> messages = new HashSet<>();

    public ErrorDTO(int status, String error, Set<ObjectError> errors) {
        this.status = status;
        this.error = error;
        this.messages = extractErrorMessages(errors);
    }

    private Set<String> extractErrorMessages(Set<ObjectError> errors){
        Set<String> errorMessages = new HashSet<>();

        Iterator<ObjectError> errorsIterator = errors.iterator();

        while(errorsIterator.hasNext()) {
            ObjectError error = errorsIterator.next();
            errorMessages.add(error.getDefaultMessage());
        }

        return errorMessages;
    }
}

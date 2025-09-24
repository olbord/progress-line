package ee.taltech.veebirakenduseprojekt.exception;

import lombok.*;
import lombok.extern.slf4j.*;
@Data
public class ApplicationException extends Throwable {
    public ApplicationException(String error) {
        String message;
    }
}

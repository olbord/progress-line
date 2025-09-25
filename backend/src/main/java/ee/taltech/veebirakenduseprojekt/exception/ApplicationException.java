package ee.taltech.veebirakenduseprojekt.exception;

import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    public ApplicationException(String error) {
        String message;
    }
}

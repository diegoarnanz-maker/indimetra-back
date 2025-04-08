package indimetra.exception;

import org.springframework.http.HttpStatus;

import indimetra.exception.base.BaseApiException;

public class ConflictException extends BaseApiException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

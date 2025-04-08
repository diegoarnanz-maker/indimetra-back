package indimetra.exception;

import org.springframework.http.HttpStatus;

import indimetra.exception.base.BaseApiException;

public class NotFoundException extends BaseApiException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

package indimetra.exception;

import org.springframework.http.HttpStatus;

import indimetra.exception.base.BaseApiException;

public class BadRequestException extends BaseApiException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

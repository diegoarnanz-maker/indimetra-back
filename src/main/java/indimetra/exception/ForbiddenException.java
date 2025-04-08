package indimetra.exception;

import org.springframework.http.HttpStatus;

import indimetra.exception.base.BaseApiException;

public class ForbiddenException extends BaseApiException {
    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

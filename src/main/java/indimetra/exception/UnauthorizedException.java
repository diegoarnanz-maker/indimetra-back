package indimetra.exception;

import org.springframework.http.HttpStatus;

import indimetra.exception.base.BaseApiException;

public class UnauthorizedException extends BaseApiException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

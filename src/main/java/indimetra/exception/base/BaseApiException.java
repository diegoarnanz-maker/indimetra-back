package indimetra.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseApiException extends RuntimeException {

    private final HttpStatus status;

    public BaseApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

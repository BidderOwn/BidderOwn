package site.bidderown.server.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serializable;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException implements Serializable {
    public NotFoundException(String message) {
        super("Not Found -> " + message);
    }

    public NotFoundException(Long message) {
        super("Not Found -> " + message);
    }
}

package site.bidderown.server.base.exception;

import java.io.Serializable;

public class NotFoundException extends RuntimeException implements Serializable {
    public NotFoundException(String message) {
        super("Not Found -> " + message);
    }
}

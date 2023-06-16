package site.bidderown.server.base.exception;

import java.io.Serializable;

public class ConflictException extends RuntimeException implements Serializable {
    public ConflictException(String message) {
        super("This data already exists. ->" + message);
    }
}

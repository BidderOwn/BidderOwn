package site.bidderown.server.base.exception.custom_exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class ForbiddenException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public ForbiddenException(String message) {
        super(errorCode, message);
    }
}

package site.bidderown.server.base.exception.custom_exception;

import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class DuflicationException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public DuflicationException(String message) {
        super(errorCode, message);
    }
}


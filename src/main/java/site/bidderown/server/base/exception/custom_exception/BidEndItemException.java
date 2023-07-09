package site.bidderown.server.base.exception.custom_exception;

import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class BidEndItemException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.BID_END;

    public BidEndItemException(String message, String logMessage){
        super(errorCode,  message, logMessage);
    }
}

package site.bidderown.server.base.exception.custom_exception;

import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class SoldOutItemException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.SOLDOUT;

    public SoldOutItemException(String message, String id){
        super(errorCode,  message, id);
    }
}

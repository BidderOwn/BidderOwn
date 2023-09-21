package site.bidderown.server.base.exception.custom_exception;

import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class WrongBidPriceException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.LOWER_BID_PRICE;

    public WrongBidPriceException(String id){
        super(errorCode, "잘못된 입찰가 제시", id);
    }
}

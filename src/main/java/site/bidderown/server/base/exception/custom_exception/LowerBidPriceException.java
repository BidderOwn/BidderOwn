package site.bidderown.server.base.exception.custom_exception;

import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

public class LowerBidPriceException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.LOWER_BID_PRICE;

    public LowerBidPriceException(Long id){
        super(errorCode, "더 낮은 값을 제시할 수 없습니다.", "Bid with a lower price. item id: " + id);
    }
}

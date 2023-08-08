package site.bidderown.server.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND, "조회하려고 하는 데이터가 존재하지 않을 경우"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없는 경우"),
    DUFLICATION(HttpStatus.BAD_REQUEST, "데이터 중복"),
    BID_END(HttpStatus.BAD_REQUEST, "입찰이 종료된 경우"),
    SOLDOUT(HttpStatus.BAD_REQUEST, "판매가 종료된 경우"),
    LOWER_BID_PRICE(HttpStatus.CONFLICT, "현재 입찰가보다 낮은 가격을 제시한 경우");

    private final HttpStatus status;
    private final String description;

    ErrorCode(HttpStatus status, String description) {
        this.status = status;
        this.description = description;
    }
}
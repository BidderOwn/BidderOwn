package site.bidderown.server.base.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException{
    /**
     * @description 허용되지 않은 사용자 (예외처리 적용 전까지 임시 적용)
     * @param message
     */
    public ForbiddenException(String message) {
        super("Forbidden -> " + message);
    }
}

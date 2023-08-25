package site.bidderown.server.base.exception.custom_exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import site.bidderown.server.base.exception.CustomException;
import site.bidderown.server.base.exception.ErrorCode;

import java.io.Serializable;


public class NotFoundException extends CustomException {
    private static final ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public NotFoundException(String message, String id){
        super(errorCode,  message, id);
    }
}

package site.bidderown.server.base.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private String logMessage;

    protected CustomException(ErrorCode errorCode, String message, String id){
        super(message);
        this.errorCode = errorCode;
        this.logMessage = message + " (" + id + ")";
    }

    protected CustomException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
        this.logMessage = message;
    }
}

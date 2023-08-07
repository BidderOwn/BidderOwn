package site.bidderown.server.base.exception;

import lombok.Getter;

@Getter
public abstract class CustomException extends RuntimeException{
    private final ErrorCode errorCode;
    private String logMessage;

    protected CustomException(ErrorCode errorCode, String message, String logMessage){
        super(message);
        this.errorCode = errorCode;
        this.logMessage = message + " (" + logMessage + ")";
    }

    protected CustomException(ErrorCode errorCode, String message){
        super(message);
        this.errorCode = errorCode;
    }

    protected CustomException(ErrorCode errorCode, String message, Long id){
        super(message);
        this.errorCode = errorCode;
        this.logMessage = message + " (" + id + ")";
    }
}

package site.bidderown.server.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.view.RedirectView;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 서비스 로직 도중 발생하는 에러들을 커스텀하여 응답값을 내려줍니다.
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        ErrorCode errorCode = e.getErrorCode();
        log.error(e.getLogMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(errorResponse);
    }

    /**
     * 바인딩 에러 json형식으로 에러가 있는 파라미터 값에 메시지를 담아 넘겨줍니다.
     *  itemPrice : 아이템 가격을 100원 이상 1,000,000,000원 이하여야 합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors()
                .forEach(c -> errors.put(((FieldError) c).getField(), c.getDefaultMessage()));
        log.warn(ex.getMessage());
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RedisConnectionFailureException.class)
    public ResponseEntity<ErrorResponse> handleRedisConnectionFailureExceptions(RedisConnectionFailureException e){
        ErrorResponse errorResponse = new ErrorResponse("서버 장애로 요청이 실패했습니다. 잠시 후 다시 시도해주세요!");
        log.error(e.getMessage());
        return ResponseEntity.status(500).body(errorResponse);
    }

}

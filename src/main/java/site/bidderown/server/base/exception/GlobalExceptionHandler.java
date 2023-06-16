package site.bidderown.server.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.view.RedirectView;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({org.springframework.validation.BindException.class})
    public RedirectView handleException(Exception ex) {
//        String alertMessage = "오류가 발생했습니다." + ex.getMessage();
//        String redirectPath = "/";
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("alertMessage", alertMessage);
//        modelAndView.setViewName("redirect:"+redirectPath);
//        return modelAndView;
        log.error(ex.getMessage());
        return new RedirectView("/");
    }
}

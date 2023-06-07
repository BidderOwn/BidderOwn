package site.bidderown.server.bounded_context.notification.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import site.bidderown.server.bounded_context.notification.controller.dto.NotificationDto;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

//    @GetMapping("/notifications")
//    public List<NotificationDto> notificationList(@AuthenticationPrincipal User user){
//        List<Notification> list = notificationService.list(user.getUsername());
//        return list;
        // return dto 변환 코드 작성해야함
//    }

}

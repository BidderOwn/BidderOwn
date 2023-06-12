package site.bidderown.server.bounded_context.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import site.bidderown.server.bounded_context.notification.controller.dto.NotificationDto;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String notificationList(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> list = notificationService.list(username);
        model.addAttribute("notifications",
                list.stream().map(notification -> NotificationDto.of(notification))
                        .collect(Collectors.toList())
        );

        return "usr/notification/list";
        // return dto 변환 코드 작성해야함
    }
}

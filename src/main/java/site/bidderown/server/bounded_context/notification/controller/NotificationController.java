package site.bidderown.server.bounded_context.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.bidderown.server.bounded_context.notification.controller.dto.NotificationResponse;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public String notificationList(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> list = notificationService.getNotifications(username);
        model.addAttribute("notifications",
                list.stream().map(NotificationResponse::of)
                        .collect(Collectors.toList())
        );

        return "usr/notification/list";
        // return dto 변환 코드 작성해야함
    }

    @GetMapping("api/v1/notification-check")
    @ResponseBody
    public Boolean checkNotification(@AuthenticationPrincipal User user) {
        if (user == null) return false;
        return notificationService.checkNotRead(user.getUsername());
    }


    @PutMapping("/notification/readAll")
    public String readAllNotification() {
        notificationService.readAll();
        return "/usr/notification/list";
    }
}
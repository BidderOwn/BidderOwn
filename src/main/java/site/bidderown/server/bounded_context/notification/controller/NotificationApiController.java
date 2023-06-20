package site.bidderown.server.bounded_context.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.notification.controller.dto.NotificationResponse;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class NotificationApiController {
    private final NotificationService notificationService;
    @GetMapping("/api/v1/notifications")
    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponse> notificationList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> list = notificationService.getNotifications(username);

        return list.stream().map(NotificationResponse::of).collect(Collectors.toList());
        // return dto 변환 코드 작성해야함
    }

    @GetMapping("/api/v1/notification-check")
    public Boolean checkNotification(@AuthenticationPrincipal User user) {
        if (user == null) return false;
        return notificationService.checkNotRead(user.getUsername());
    }

    @PutMapping("/api/v1/notification/readAll")
    public void readAllNotification() {
        notificationService.readAll();
    }
}

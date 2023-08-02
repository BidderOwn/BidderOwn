package site.bidderown.server.boundedcontext.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.boundedcontext.notification.controller.dto.NotificationResponse;
import site.bidderown.server.boundedcontext.notification.entity.Notification;
import site.bidderown.server.boundedcontext.notification.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "알림 notification-api-controller", description = "알림 관련 api 입니다.")
@RequiredArgsConstructor
public class NotificationApiController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 목록", description = "사용자의 모든 알림을 조회합니다.")
    @GetMapping("/api/v1/notifications")
    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponse> notificationList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> list = notificationService.getNotifications(username);

        return list.stream().map(NotificationResponse::of).collect(Collectors.toList());
        // return dto 변환 코드 작성해야함
    }

    @Operation(summary = "알림 확인 여부", description = "사용자의 알림 확인 여부를 반환합니다. 알림 미확인 시 아이콘에 빨간불이 들어옵니다.")
    @GetMapping("/api/v1/notification-check")
    public Boolean checkNotification(@AuthenticationPrincipal User user) {
        if (user == null) return false;
        return notificationService.checkNotRead(user.getUsername());
    }

    @Operation(summary = "알림 전체 읽음 처리", description = "알림 모두 지우기 버튼을 눌렀을 때 모든 알림을 읽을 처리합니다.")
    @PutMapping("/api/v1/notification/readAll")
    public void readAllNotification(@AuthenticationPrincipal User user) {
        notificationService.readAll(user.getUsername());
    }
}

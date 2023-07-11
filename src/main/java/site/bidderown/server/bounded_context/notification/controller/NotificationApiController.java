package site.bidderown.server.bounded_context.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.notification.controller.dto.NotificationResponse;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "notification", description = "알림 API")
@RestController
@RequiredArgsConstructor
public class NotificationApiController {
    private final NotificationService notificationService;

    @Operation(summary = "알림 리스트 조회", description = "username을 이용하여 모든 notification 리스트를 조회합니다.")
    @GetMapping("/api/v1/notifications")
    @PreAuthorize("isAuthenticated()")
    public List<NotificationResponse> notificationList() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Notification> list = notificationService.getNotifications(username);

        return list.stream().map(NotificationResponse::of).collect(Collectors.toList());
        // return dto 변환 코드 작성해야함
    }

    @Operation(summary = "알림 체크", description = "username을 이용하여 읽지 않은 알림이 있는지 확인합니다.")
    @GetMapping("/api/v1/notification-check")
    public Boolean checkNotification(@AuthenticationPrincipal User user) {
        if (user == null) return false;
        return notificationService.checkNotRead(user.getUsername());
    }

    @Operation(summary = "모든 알림 읽기", description = "username을 이용하여 모든 알림을 읽습니다.")
    @ApiResponses ({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND")
    })
    @PutMapping("/api/v1/notification/readAll")
    public void readAllNotification(@AuthenticationPrincipal User user) {
        notificationService.readAll(user.getUsername());
    }
}

package site.bidderown.server.bounded_context.notification.controller.dto;


import lombok.*;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {

    private Long itemId;
    private LocalDateTime createdDate;
    private NotificationType notificationType;

    @Builder
    private NotificationResponse(Long itemId, LocalDateTime createdDate, NotificationType notificationType) {
        this.itemId = itemId;
        this.createdDate = createdDate;
        this.notificationType = notificationType;
    }

    public static NotificationResponse of(Notification notification){
        return NotificationResponse.builder()
                .createdDate(notification.getCreatedAt())
                .notificationType(notification.getNotificationType())
                .itemId(notification.getItem().getId()).build();
    }
}
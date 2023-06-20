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
    private String createdDate;
    private LocalDateTime readDate;
    private NotificationType notificationType;

    @Builder
    private NotificationResponse(Long itemId, String createdDate, LocalDateTime readDate, NotificationType notificationType) {
        this.itemId = itemId;
        this.createdDate = createdDate;
        this.readDate = readDate;
        this.notificationType = notificationType;
    }

    public static NotificationResponse of(Notification notification){
        Duration duration = Duration.between(notification.getCreatedAt(), LocalDateTime.now());
        String createdAt;

        if(duration.toMinutes() < 1)
        {
            createdAt = "방금 전";
        } else if (duration.toMinutes() < 60) {
            createdAt = duration.toMinutes() + "분 전";
        } else if (duration.toMinutes() < 1440) {
            createdAt = duration.toHours() + "시간 전";
        }
        else
            createdAt = duration.toDays() + "일 전";


        return NotificationResponse.builder()
                .createdDate(createdAt)
                .notificationType(notification.getNotificationType())
                .readDate(notification.getReadDate())
                .itemId(notification.getItem().getId()).build();
    }
}

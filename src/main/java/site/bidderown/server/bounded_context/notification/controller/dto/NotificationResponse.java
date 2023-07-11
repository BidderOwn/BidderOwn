package site.bidderown.server.bounded_context.notification.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

import java.time.Duration;
import java.time.LocalDateTime;

@Schema(description = "알림 응답")
@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {
    @Schema(description = "상품 ID")
    private Long itemId;
    @Schema(description = "생성 일자")
    private LocalDateTime createdDate;
    @Schema(description = "알림 타입", allowableValues = {"BID","SOLDOUT","BID_END","COMMENT"})
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
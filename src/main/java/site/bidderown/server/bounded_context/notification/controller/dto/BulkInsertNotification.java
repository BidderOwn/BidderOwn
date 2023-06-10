package site.bidderown.server.bounded_context.notification.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.bid.entity.BidResult;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BulkInsertNotification {

    private Long itemId;
    private Long receiverId;
    private String notificationType;
    private LocalDateTime readDate;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @Builder
    public BulkInsertNotification(Long itemId, Long receiverId) {
        this.itemId = itemId;
        this.receiverId = receiverId;
        this.notificationType = String.valueOf(NotificationType.BID_END);
        this.readDate = null;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
}
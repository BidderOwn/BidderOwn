package site.bidderown.server.bounded_context.notification.repository.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JdbcNotification {
    private LocalDateTime readDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long itemId;
    private Long receiverId;
    private String notificationType;

    @Builder
    public JdbcNotification(Long itemId, Long receiverId, String notificationType) {
        LocalDateTime now = LocalDateTime.now();
        this.readDate = null;
        this.createdAt = now;
        this.updatedAt = now;
        this.itemId = itemId;
        this.receiverId = receiverId;
        this.notificationType = notificationType;
    }

    public static JdbcNotification of(Long itemId, Long receiverId, String notificationType) {
        return JdbcNotification.builder()
                .itemId(itemId)
                .receiverId(receiverId)
                .notificationType(notificationType)
                .build();
    }
}

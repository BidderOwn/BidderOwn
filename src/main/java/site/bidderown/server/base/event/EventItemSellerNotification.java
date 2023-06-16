package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

@Getter
@NoArgsConstructor
public class EventItemSellerNotification {
    private Item item;
    private NotificationType type;

    @Builder
    public EventItemSellerNotification(Item item) {
        this.item = item;
        this.type = NotificationType.COMMENT;
    }

    public static EventItemSellerNotification of(Item item) {
        return EventItemSellerNotification.builder()
                .item(item)
                .build();
    }
}

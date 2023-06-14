package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

@Getter
@NoArgsConstructor
public class EventSoldOutNotification {
    private Item item;
    private NotificationType type;

    @Builder
    public EventSoldOutNotification(Item item) {
        this.item = item;
        this.type = NotificationType.SOLDOUT;
    }

    public static EventSoldOutNotification of(Item item) {
        return EventSoldOutNotification.builder()
                .item(item)
                .build();
    }
}

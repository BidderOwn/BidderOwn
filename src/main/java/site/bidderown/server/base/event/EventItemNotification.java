package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

@Getter
@NoArgsConstructor
public class EventItemNotification {
    private Item item;
    private Member receiver;
    private NotificationType type;

    @Builder
    public EventItemNotification(Item item, Member receiver, NotificationType type) {
        this.item = item;
        this.receiver = receiver;
        this.type = type;
    }

    public static EventItemNotification of(Item item, Member receiver, NotificationType type) {
        return EventItemNotification.builder()
                .item(item)
                .receiver(receiver)
                .type(type)
                .build();
    }
}

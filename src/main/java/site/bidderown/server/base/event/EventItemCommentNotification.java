package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

@Getter
@NoArgsConstructor
public class EventItemCommentNotification {
    private Item item;
    private Member sender;
    private NotificationType type;

    @Builder
    public EventItemCommentNotification(Item item, Member sender) {
        this.item = item;
        this.sender = sender;
        this.type = NotificationType.COMMENT;
    }

    public static EventItemCommentNotification of(Item item, Member sender) {
        return EventItemCommentNotification.builder()
                .item(item)
                .sender(sender)
                .build();
    }
}

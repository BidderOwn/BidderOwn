package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventBidEndNotification {
    private List<? extends Notification> itemIds;

    @Builder
    public EventBidEndNotification(List<? extends Notification> itemIds) {
        this.itemIds = itemIds;
    }

    public static EventBidEndNotification of(List<? extends Notification> items) {
        return EventBidEndNotification.builder()
                .itemIds(items)
                .build();
    }
}

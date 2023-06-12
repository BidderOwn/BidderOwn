package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;

@Getter
@NoArgsConstructor
public class EventBidEndNotification {
    private List<? extends Item> items;

    @Builder
    public EventBidEndNotification(List<? extends Item> items) {
        this.items = items;
    }

    public static EventBidEndNotification of(List<? extends Item> items) {
        return EventBidEndNotification.builder()
                .items(items)
                .build();
    }
}

package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.boundedcontext.item.entity.Item;

@Getter
@NoArgsConstructor
public class ItemCommentAlarmEvent {
    private Item item;

    @Builder
    public ItemCommentAlarmEvent(Item item) {
        this.item = item;
    }

    public static ItemCommentAlarmEvent of(Item item) {
        return ItemCommentAlarmEvent.builder()
                .item(item)
                .build();
    }
}

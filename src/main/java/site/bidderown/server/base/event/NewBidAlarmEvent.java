package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.boundedcontext.item.entity.Item;

@Getter
@NoArgsConstructor
public class NewBidAlarmEvent {
    private Item item;

    @Builder
    public NewBidAlarmEvent(Item item) {
        this.item = item;
    }

    public static NewBidAlarmEvent of(Item item) {
        return NewBidAlarmEvent.builder()
                .item(item)
                .build();
    }
}

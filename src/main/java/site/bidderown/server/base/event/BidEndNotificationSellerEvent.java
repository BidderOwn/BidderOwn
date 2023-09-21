package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.boundedcontext.item.entity.Item;

import java.util.List;

@Getter
@NoArgsConstructor
public class BidEndNotificationSellerEvent {
    private List<? extends Item> items;

    @Builder
    public BidEndNotificationSellerEvent(List<? extends Item> items) {
        this.items = items;
    }

    public static BidEndNotificationSellerEvent of(List<? extends Item> items) {
        return BidEndNotificationSellerEvent.builder()
                .items(items)
                .build();
    }
}

package site.bidderown.server.base.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BidEndEvent {
    private Long itemId;

    @Builder
    private BidEndEvent(Long itemId) {
        this.itemId = itemId;
    }

    public static BidEndEvent of(Long itemId) {
        return BidEndEvent.builder().itemId(itemId).build();
    }
}

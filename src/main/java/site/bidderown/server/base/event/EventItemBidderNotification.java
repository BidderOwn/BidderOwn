package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

@Getter
@NoArgsConstructor
public class EventItemBidderNotification {
    private Item item;
    private Member bidder;
    private NotificationType type;

    @Builder
    public EventItemBidderNotification(Item item, Member bidder) {
        this.item = item;
        this.bidder = bidder;
        this.type = NotificationType.BID;
    }

    public static EventItemBidderNotification of(Item item, Member bidder) {
        return EventItemBidderNotification.builder()
                .item(item)
                .bidder(bidder)
                .build();
    }
}

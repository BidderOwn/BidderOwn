package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BidEndNotificationEvent {
    private List<Long> bidderIds;

    @Builder
    public BidEndNotificationEvent(List<Long> bidderIds) {
        this.bidderIds = bidderIds;
    }

    public static BidEndNotificationEvent of(List<? extends Bid> bids) {
        // 입찰자 알림 전송
        List<Long> bidderIds = bids.stream()
                .map(bid -> bid.getBidder().getId())
                .collect(Collectors.toList());

        // 판매자 알림 전송
        bidderIds.add(bids.get(0).getItem().getMember().getId());
        return BidEndNotificationEvent.builder()
                .bidderIds(bidderIds)
                .build();
    }
}

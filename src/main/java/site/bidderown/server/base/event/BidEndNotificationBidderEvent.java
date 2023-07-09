package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class BidEndNotificationBidderEvent {
    private List<Long> receiverIds;

    @Builder
    public BidEndNotificationBidderEvent(List<Long> receiverIds) {
        this.receiverIds = receiverIds;
    }

    public static BidEndNotificationBidderEvent of(List<? extends Notification> notifications) {
        List<Long> receiverIds = notifications.stream()
                .map(notification -> notification.getReceiver().getId())
                .collect(Collectors.toList());
        // 입찰자 알림 전송

        return BidEndNotificationBidderEvent.builder()
                .receiverIds(receiverIds)
                .build();
    }
}

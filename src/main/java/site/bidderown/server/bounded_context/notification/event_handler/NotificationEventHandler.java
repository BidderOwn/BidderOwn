package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventBidEndNotification;
import site.bidderown.server.base.event.EventItemNotification;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BidRepository bidRepository;

    @EventListener
    @Async
    public void listen(EventItemNotification eventItemNotification) {
        notificationService.create(eventItemNotification);
        messagingTemplate.convertAndSend(
                "/sub/notification/item/" + eventItemNotification.getItem().getId(), "");
    }

    @EventListener
    @Async
    public void listenBidEnd(EventBidEndNotification eventBidEndNotification) {
        List<Notification> notifications = new ArrayList<>();
        for (Item item : eventBidEndNotification.getItems()) {
            messagingTemplate.convertAndSend("/sub/notification/item/" + item.getId(), "");
            List<Bid> bids = bidRepository.findByItem(item);
            bids.forEach(bid -> notifications.add(Notification.of(item, bid.getBidder(), NotificationType.BID_END)));
        }
        notificationService.create(notifications);
    }
}

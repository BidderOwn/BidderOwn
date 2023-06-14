package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventBidEndNotification;
import site.bidderown.server.base.event.EventItemSellerNotification;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.controller.dto.BulkInsertNotification;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;
import site.bidderown.server.bounded_context.notification.service.NotificationService;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final BidRepository bidRepository;
    private final NotificationJdbcRepository notificationJdbcRepository;

    @EventListener
    @Async
    public void listen(EventItemBidderNotification eventItemBidderNotification) {
        Item item = eventItemBidderNotification.getItem();
        List<Bid> bids = item.getBids();
        List<Notification> notifications = new ArrayList<>();

        bids.stream()
                .filter(bid -> !bid.getBidder().equals(eventItemBidderNotification.getBidder()))
                .map(bid -> Notification.of(
                        eventItemBidderNotification.getItem(),
                        bid.getBidder(),
                        NotificationType.BID
                )).forEach(notifications::add);

        notifications.add(Notification.of(item, item.getMember(), NotificationType.BID));

        notificationService.createNotifications(notifications);

        messagingTemplate.convertAndSend(
                ConnectionType.ITEM_BIDDER.getSocketPath() + eventItemBidderNotification.getItem().getId(),
                eventItemBidderNotification.getBidder().getName());

        messagingTemplate.convertAndSend(
                ConnectionType.ITEM_SELLER.getSocketPath() + eventItemBidderNotification.getItem().getId(),
                "");
    }

    @EventListener
    @Async
    public void listen(EventItemSellerNotification eventItemSellerNotification) {
        notificationService.create(eventItemSellerNotification);

        messagingTemplate.convertAndSend(
                ConnectionType.ITEM_SELLER.getSocketPath() + eventItemSellerNotification.getItem().getId(),
                "");
    }

    @EventListener
    @Async
    public void listen(EventBidEndNotification eventBidEndNotification) {
        List<BulkInsertNotification> notifications = new ArrayList<>();
        for (Item item : eventBidEndNotification.getItems()) {
            messagingTemplate.convertAndSend(ConnectionType.ITEM_BIDDER.getSocketPath() + item.getId(), "");
            List<Bid> bids = bidRepository.findByItem(item);
            bids.forEach(bid -> notifications.add(
                    BulkInsertNotification.of(
                            item.getId(),
                            bid.getBidder().getId(),
                            NotificationType.BID_END))
            );
        }

        notificationJdbcRepository.insertNotificationList(notifications);

        /*
        List<Notification> notifications = new ArrayList<>();
        for (Item item : eventBidEndNotification.getItems()) {
            messagingTemplate.convertAndSend("/sub/notification/item/" + item.getId(), "");
            List<Bid> bids = bidRepository.findByItem(item);
            bids.forEach(bid -> notifications.add(Notification.of(item, bid.getBidder(), NotificationType.BID_END)));
        }
        notificationService.create(notifications);
        */
    }
}

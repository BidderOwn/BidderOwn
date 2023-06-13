package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventBidEndNotification;
import site.bidderown.server.base.event.EventItemCommentNotification;
import site.bidderown.server.base.event.EventItemBidNotification;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.controller.dto.BulkInsertNotification;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public void listen(EventItemBidNotification eventItemBidNotification) {
        Item item = eventItemBidNotification.getItem();
        List<Notification> notifications = new ArrayList<>();
        item.getBids().stream()
                .map(bid -> Notification.of(
                        eventItemBidNotification.getItem(),
                        bid.getBidder(),
                        NotificationType.BID
                )).forEach(notifications::add);

        notifications.add(Notification.of(item, item.getMember(), NotificationType.BID));

        notificationService.createNotifications(notifications);

        messagingTemplate.convertAndSend(
                "/sub/notification/item/" + eventItemBidNotification.getItem().getId(), "");
    }

    @EventListener
    @Async
    public void listen(EventItemCommentNotification eventItemCommentNotification) {
        notificationService.create(eventItemCommentNotification);

        messagingTemplate.convertAndSend(
                "/sub/item/comment/notification/" + eventItemCommentNotification.getItem().getId(),
                eventItemCommentNotification.getSender().getName());
    }

    @EventListener
    @Async
    public void listenBidEnd(EventBidEndNotification eventBidEndNotification) {
        List<BulkInsertNotification> notifications = new ArrayList<>();
        for (Item item : eventBidEndNotification.getItems()) {
            messagingTemplate.convertAndSend("/sub/notification/item/" + item.getId(), "");
            List<Bid> bids = bidRepository.findByItem(item);
            bids.forEach(bid -> notifications.add(BulkInsertNotification.builder()
                    .itemId(item.getId())
                    .receiverId(bid.getBidder().getId()).build()));
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

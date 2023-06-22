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
import site.bidderown.server.base.event.EventSoldOutNotification;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
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


    /**
     * @description 아이템에 입찰가를 제시한 사람(입찰자)에게 알림을 전송
     * @param eventItemBidderNotification
     * Item: 입찰가가 제시된 상품, Member: 입찰자, NotificationType 알림 타입
     */
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

    /**
     * @description 상품 판매자에게 알림을 보냄
     * @param eventItemSellerNotification 상품 엔티티, 알림 타입
     */
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
    public void listen(EventSoldOutNotification eventSoldOutNotification) {
        Item item = eventSoldOutNotification.getItem();
        List<Bid> bids = item.getBids();
        List<Notification> notifications = new ArrayList<>();

        bids.stream().map(bid -> Notification.of(item, bid.getBidder(), NotificationType.SOLDOUT))
                .forEach(notifications::add);

        notificationService.create(notifications);

        messagingTemplate.convertAndSend(
                ConnectionType.ITEM_BIDDER.getSocketPath() + eventSoldOutNotification.getItem().getId(),
                "");
    }

    @EventListener
    @Async
    public void listen(EventBidEndNotification eventBidEndNotification) {
        for (Notification notification : eventBidEndNotification.getItemIds()){
            messagingTemplate.convertAndSend(
                    ConnectionType.ITEM_BIDDER.getSocketPath() + notification.getItem().getId(),
                    "");
        }
    }
}

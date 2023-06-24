package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.BidEndNotificationBidderEvent;
import site.bidderown.server.base.event.BidEndNotificationSellerEvent;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;

    @Value("${custom.socket.path}")
    private String socketPath;

    @Value("${custom.socket.alarm_type_notification}")
    private String ALARM_TYPE;

    @EventListener
    @Async
    public void listen(BidEndNotificationBidderEvent bidEndNotificationBidderEvent) {
        bidEndNotificationBidderEvent.getReceiverIds()
                .forEach(bidderId -> messagingTemplate.convertAndSend(socketPath + bidderId, ALARM_TYPE));
    }

    @EventListener
    @Async
    public void listen(BidEndNotificationSellerEvent bidEndNotificationSellerEvent) {
        List<Notification> notifications = new ArrayList<>();

        bidEndNotificationSellerEvent.getItems()
                .forEach(item -> {
                    notifications.add(Notification.of(item, item.getMember(), NotificationType.BID_END));
                    messagingTemplate.convertAndSend(socketPath + item.getMember().getId(), ALARM_TYPE);
                });
        notificationService.create(notifications);
    }
}

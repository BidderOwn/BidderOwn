package site.bidderown.server.boundedcontext.notification.eventhandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.BidEndEvent;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.notification.entity.Notification;
import site.bidderown.server.boundedcontext.notification.entity.NotificationType;
import site.bidderown.server.boundedcontext.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationService notificationService;
    private final ItemService itemService;

    @Value("${custom.socket.path}")
    private String socketPath;

    @Value("${custom.socket.alarm_type_notification}")
    private String ALARM_TYPE;

    @EventListener
    @Async
    @Transactional
    public void listen(BidEndEvent bidEndEvent) {
        notificationService.createAndSendBidEndNotification(bidEndEvent.getItemId());
    }
}

package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.BidEndNotificationEvent;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${custom.socket.path}")
    private String socketPath;

    @Value("${custom.socket.alarm_type_notification}")
    private String ALARM_TYPE;

    @EventListener
    @Async
    public void listen(BidEndNotificationEvent bidEndNotificationEvent) {
        bidEndNotificationEvent.getBidderIds().stream()
                .forEach(bidderId -> messagingTemplate.convertAndSend(socketPath + bidderId, ALARM_TYPE));
    }
}

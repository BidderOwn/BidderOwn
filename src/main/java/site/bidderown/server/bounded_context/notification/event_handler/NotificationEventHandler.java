package site.bidderown.server.bounded_context.notification.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventItemNotification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventHandler {

    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;

    @EventListener
    public void listen(EventItemNotification eventItemNotification) {

        log.info("EventItemNotification listen");

        notificationService.create(eventItemNotification);
        messagingTemplate.convertAndSend(
                "/sub/notification/item/" + eventItemNotification.getItem().getId(), "");
    }
}

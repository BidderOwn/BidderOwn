package site.bidderown.server.batch.item.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.context.ApplicationEventPublisher;
import site.bidderown.server.base.event.BidEndNotificationBidderEvent;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BidEndNotificationWriterListener implements ItemWriteListener<Notification> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void beforeWrite(List<? extends Notification> items) {

    }

    @Override
    public void afterWrite(List<? extends Notification> notifications) {
        publisher.publishEvent(BidEndNotificationBidderEvent.of(notifications));
    }

    @Override
    public void onWriteError(Exception exception, List items) {

    }
}

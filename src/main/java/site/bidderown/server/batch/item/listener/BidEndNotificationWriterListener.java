package site.bidderown.server.batch.item.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.context.ApplicationEventPublisher;
import site.bidderown.server.base.event.BidEndNotificationEvent;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BidEndNotificationWriterListener implements ItemWriteListener<Bid> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void beforeWrite(List<? extends Bid> items) {

    }

    @Override
    public void afterWrite(List<? extends Bid> bidEndNotifications) {
        publisher.publishEvent(BidEndNotificationEvent.of(bidEndNotifications));
    }

    @Override
    public void onWriteError(Exception exception, List items) {

    }
}

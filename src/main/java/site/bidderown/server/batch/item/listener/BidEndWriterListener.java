package site.bidderown.server.batch.item.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.context.ApplicationEventPublisher;
import site.bidderown.server.base.event.BidEndNotificationSellerEvent;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;

@RequiredArgsConstructor
public class BidEndWriterListener implements ItemWriteListener<Item> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void beforeWrite(List<? extends Item> items) {
        publisher.publishEvent(BidEndNotificationSellerEvent.of(items));
    }

    @Override
    public void afterWrite(List<? extends Item> items) {

    }

    @Override
    public void onWriteError(Exception exception, List<? extends Item> items) {

    }
}

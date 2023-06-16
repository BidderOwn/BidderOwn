package site.bidderown.server.batch.item.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.context.ApplicationEventPublisher;
import site.bidderown.server.base.event.EventBidEndNotification;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BidEndItemWriterListener implements ItemWriteListener<Item> {

    private final ApplicationEventPublisher publisher;

    @Override
    public void beforeWrite(List<? extends Item> items) {

    }

    @Override
    public void afterWrite(List<? extends Item> items) {
        publisher.publishEvent(EventBidEndNotification.of(items));
    }

    @Override
    public void onWriteError(Exception exception, List items) {

    }
}

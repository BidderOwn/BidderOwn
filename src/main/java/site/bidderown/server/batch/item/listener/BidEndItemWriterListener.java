package site.bidderown.server.batch.item.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventItemNotification;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;

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
        // TODO QueryDsl 적용 해야될 듯
        // item, bid join -> EventItemNotification 으로 받으면 될 듯
        items.stream()
                .forEach(item -> item.getBids().stream()
                        .forEach(bid -> {
                            publisher.publishEvent(
                                    EventItemNotification.of(item, bid.getBidder(), NotificationType.BID_END)
                            );
                        }));
    }

    @Override
    public void onWriteError(Exception exception, List items) {

    }
}

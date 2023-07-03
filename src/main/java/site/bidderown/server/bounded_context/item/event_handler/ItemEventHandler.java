package site.bidderown.server.bounded_context.item.event_handler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.BidEndEvent;
import site.bidderown.server.bounded_context.item.service.ItemService;

@Component
@RequiredArgsConstructor
public class ItemEventHandler {

    private final ItemService itemService;

    @EventListener
    @Async
    public void listen(BidEndEvent bidEndEvent) {
        itemService.bidEndItem(bidEndEvent.getItemId());
    }
}

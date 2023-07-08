package site.bidderown.server.bounded_context.bid.entitylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

@Slf4j
@RequiredArgsConstructor
@Component
public class BidEntityListener {

    private final ItemCountBuffer itemCountBuffer;

    @PostPersist
    public void postPersist(Bid bid) {
        log.info("bid post persist {}", bid.getId());
        itemCountBuffer.push(BidCountTask.of("bid", bid.getItem().getId()));
    }
}

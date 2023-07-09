package site.bidderown.server.bounded_context.bid.entitylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

/**
 * Bid Entity 추가 이벤트 리스너
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class BidEntityListener {

    private final ItemCountBuffer itemCountBuffer;

    @Value("${custom.buffer-task.type.bid}")
    private String type;

    @PostPersist
    public void postPersist(Bid bid) {
        log.info("bid post persist {}", bid.getId());
        itemCountBuffer.push(BidCountTask.of(type, bid.getItem().getId()));
    }
}

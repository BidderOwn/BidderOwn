package site.bidderown.server.bounded_context.bid.entitylistener;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

/**
 * Bid Entity 추가 이벤트 리스너
 */

@Slf4j
@NoArgsConstructor
public class BidEntityListener {

    @Value("${custom.buffer-task.type.bid}")
    private String type;

    @PostPersist
    public void postPersist(Bid bid) {
        ItemCountBuffer itemCountBuffer = BeanUtils.getBean(ItemCountBuffer.class);
        itemCountBuffer.push(BidCountTask.of(type, bid.getItem().getId()));
    }
}

package site.bidderown.server.bounded_context.bid.entitylistener;

import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;

public class BidEntityListener {

    @PostPersist
    public void postPersist(Bid bid) {
        ItemRedisService itemRedisService = BeanUtils.getBean(ItemRedisService.class);
        itemRedisService.increaseBidScore(bid.getItem().getId());
    }

    @PostRemove
    public void postRemove(Bid bid) {
        ItemRedisService itemRedisService = BeanUtils.getBean(ItemRedisService.class);
        itemRedisService.decreaseBidScore(bid.getItem().getId());
    }
}

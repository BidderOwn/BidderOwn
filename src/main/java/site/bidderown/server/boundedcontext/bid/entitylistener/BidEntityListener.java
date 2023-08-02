package site.bidderown.server.boundedcontext.bid.entitylistener;

import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.item.service.ItemRedisService;

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

package site.bidderown.server.boundedcontext.item.entitylistener;

import lombok.NoArgsConstructor;
import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;
import site.bidderown.server.boundedcontext.item.service.ItemRedisService;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.Period;

@NoArgsConstructor
public class ItemEntityListener {

/*
    @PostUpdate
    public void postUpdate(Item item) {
        if (!item.getItemStatus().equals(ItemStatus.BIDDING)) {
            ItemRedisService itemRedisService = BeanUtils.getBean(ItemRedisService.class);
            itemRedisService.removeBidRankingKey(item.getId());
        }
    }
    */

}

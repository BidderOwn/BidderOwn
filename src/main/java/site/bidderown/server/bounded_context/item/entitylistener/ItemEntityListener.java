package site.bidderown.server.bounded_context.item.entitylistener;

import lombok.NoArgsConstructor;
import site.bidderown.server.base.util.BeanUtils;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import java.time.Period;

@NoArgsConstructor
public class ItemEntityListener {

    @PostPersist
    public void postPersist(Item item) {
        ItemRedisService itemRedisService = BeanUtils.getBean(ItemRedisService.class);
        itemRedisService.createWithExpire(item, genExpireDay(item));
    }

    @PostUpdate
    public void postUpdate(Item item) {
        if (!item.getItemStatus().equals(ItemStatus.BIDDING)) {
            ItemRedisService itemRedisService = BeanUtils.getBean(ItemRedisService.class);
            itemRedisService.removeBidRankingKey(item.getId());
        }
    }

    private int genExpireDay(Item item) {
        return Period.between(
                item.getCreatedAt().toLocalDate(),
                item.getExpireAt().toLocalDate()
        ).getDays();
    }
}

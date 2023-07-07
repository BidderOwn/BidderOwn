package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemRedisService {

    @Value("${custom.redis.item.bidding.comment-count-key}")
    private String commentCountKey;

    @Value("${custom.redis.item.bidding.bid-count-key}")
    private String bidCountKey;

    private final ItemRedisRepository itemRedisRepository;

    public boolean containsKey(Item item) {
        return itemRedisRepository.contains(item.getId());
    }

    public void createWithExpire(Item item, int expire) {
        itemRedisRepository.save(item.getId(), expire);
    }

    public void increaseCommentCount(List<Long> itemIds) {
        itemIds.forEach(itemId -> itemRedisRepository.increaseValue(itemId, commentCountKey));
    }

    public void increaseBidCount(List<Long> itemIds) {
        itemIds.forEach(itemId -> itemRedisRepository.increaseValue(itemId, bidCountKey));
    }

    public int getCommentCount(Long itemId) {
        return itemRedisRepository.getCommentCount(itemId);
    }

    public int getBidCount(Long itemId) {
        return itemRedisRepository.getBidCount(itemId);
    }
}

package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemRedisService {

    private final ItemRedisRepository itemRedisRepository;

    public boolean containsKey(Item item) {
        return itemRedisRepository.contains(item.getId());
    }

    public void createWithExpire(Item item, int expire) {
        itemRedisRepository.save(item.getId(), expire);
    }

    public ItemCountResponse getItemCounts(Long itemId) {
        return itemRedisRepository.getItemCounts(itemId);
    }

    public Optional<Integer> getCommentCount(Long itemId) {
        return itemRedisRepository.getCommentCount(itemId);
    }

    public Optional<Integer> getBidCount(Long itemId) {
        return itemRedisRepository.getBidCount(itemId);
    }

    public Optional<Integer> getHeartCount(Long itemId) {
        return itemRedisRepository.getHeartCount(itemId);
    }

    public void increaseCount(Long itemId, String type) {
        itemRedisRepository.increaseValue(itemId, type + "-count");
    }
}

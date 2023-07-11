package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bidderown.server.base.redis.buffer.BufferTask;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;
import site.bidderown.server.bounded_context.item.repository.dto.ItemCounts;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ItemRedisService {

    private final ItemRedisRepository itemRedisRepository;

    @Value("${custom.redis.item.bidding.count-suffix}")
    private String countSuffix;

    public boolean containsKey(Item item) {
        return itemRedisRepository.contains(item.getId());
    }

    public void createWithExpire(Item item, int expire) {
        itemRedisRepository.save(item.getId(), expire);
    }

    /**
     * item 의 count 정보를 한번에 가져오는 메서드
     */
    public ItemCounts getItemCounts(Item item) {
        return itemRedisRepository.getItemCounts(item.getId())
                .orElseGet(() -> ItemCounts.of(
                        item.getBids().size(),
                        item.getComments().size(),
                        item.getHearts().size()
                ));
    }

    /**
     * Redis 의 pipelining 을 사용하여서 효율적으로 증가
     * @param tasks
     */
    public void increaseItemCounts(List<BufferTask> tasks) {
        itemRedisRepository.increaseItemCountsWithPipelined(tasks);
    }
}

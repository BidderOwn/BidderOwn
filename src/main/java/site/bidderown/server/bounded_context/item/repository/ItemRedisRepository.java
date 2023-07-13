package site.bidderown.server.bounded_context.item.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import site.bidderown.server.base.redis.buffer.CountTask;
import site.bidderown.server.bounded_context.item.repository.dto.ItemCounts;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 아이템 count 정보가 들어있는 redis repository
 * 경매 종료시간에 맞춰 expire time 을 설정해 두었습니다.
 */

@RequiredArgsConstructor
@Repository
public class ItemRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private HashOperations<String, String, Integer> hashCountOperations;

    @Value("${custom.redis.item.bidding.info-key}")
    private String biddingItemInfoKey;

    @Value("${custom.redis.item.bidding.count-suffix}")
    private String countSuffix;

    @PostConstruct
    private void init() {
        hashCountOperations = redisTemplate.opsForHash();
    }

    public void save(Long itemId, int day) {
        Map<String, Integer> itemCountResponseMap = objectMapper.convertValue(ItemCounts.newInstance(), Map.class);
        hashCountOperations.putAll(biddingItemInfoKey + itemId, itemCountResponseMap);
        redisTemplate.expire(biddingItemInfoKey + itemId, day, TimeUnit.DAYS);
    }

    public boolean contains(Long itemId) {
        return redisTemplate.hasKey(biddingItemInfoKey + itemId) != null;
    }

    public Optional<ItemCounts> getItemCounts(Long itemId) {
        Map<String, Integer> entries = hashCountOperations.entries(biddingItemInfoKey + itemId);

        if (entries.keySet().size() == 0) return Optional.empty();

        ItemCounts itemCounts =  objectMapper.convertValue(entries, ItemCounts.class);
        return Optional.ofNullable(itemCounts);
    }

    public void handleTasksWithPipelined(List<CountTask> tasks) {
        redisTemplate.executePipelined((RedisCallback<?>) connection -> {
            for (CountTask task : tasks) {
                connection.hIncrBy(
                        (biddingItemInfoKey + task.getId()).getBytes(),
                        (task.getType() + countSuffix).getBytes(),
                        task.getDelta()
                );
            }
            return null;
        });
    }
}


package site.bidderown.server.bounded_context.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ItemExpirationQueueRepository {

    @Value("${custom.redis.item_queue}")
    private String key;

    private final RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperation;

    @PostConstruct
    private void init() {
        valueOperation = redisTemplate.opsForValue();
    }

    public void save(Long itemId, int day) {
        valueOperation.set(key + itemId, "", day, TimeUnit.DAYS);
    }

    public boolean contains(Long itemId) {
        return !Objects.isNull(valueOperation.get(key + itemId));
    }
}


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
public class ItemRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOperations;

    @Value("${custom.redis.item.bidding.info-key}")
    private String biddingItemInfoKey;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
    }

    public void save(Long itemId, int day) {
        valueOperations.set(biddingItemInfoKey + itemId, "", day, TimeUnit.DAYS);
    }

    public boolean contains(Long itemId) {
        return !Objects.isNull(valueOperations.get(biddingItemInfoKey + itemId));
    }
}


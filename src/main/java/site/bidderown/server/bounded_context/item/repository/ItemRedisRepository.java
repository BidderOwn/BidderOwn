package site.bidderown.server.bounded_context.item.repository;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
public class ItemRedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private HashOperations<String, String, Integer> hashCountOperations;

    @Value("${custom.redis.item.bidding.info-key}")
    private String biddingItemInfoKey;

    @Value("${custom.redis.item.bidding.comment-count-key}")
    private String commentCountKey;

    @Value("${custom.redis.item.bidding.bid-count-key}")
    private String bidCountKey;

    @Value("${custom.redis.item.bidding.heart-count-key}")
    private String heartCountKey;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)

    @PostConstruct
    private void init() {
        hashCountOperations = redisTemplate.opsForHash();
    }

    public void save(Long itemId, int day) {
        hashCountOperations.put(biddingItemInfoKey + itemId, commentCountKey, 0);
        hashCountOperations.put(biddingItemInfoKey + itemId, bidCountKey, 0);
        hashCountOperations.put(biddingItemInfoKey + itemId, heartCountKey, 0);
        redisTemplate.expire(biddingItemInfoKey + itemId, day, TimeUnit.DAYS);
    }

    public boolean contains(Long itemId) {
        return redisTemplate.hasKey(biddingItemInfoKey + itemId) != null;
    }

    public void increaseValue(Long itemId, String key) {
        hashCountOperations.increment(biddingItemInfoKey + itemId, key, 1);
    }

    public Optional<Integer> getCommentCount(Long itemId) {
        return Optional.ofNullable(hashCountOperations.get(biddingItemInfoKey + itemId, commentCountKey));
    }

    public Optional<Integer> getBidCount(Long itemId) {
        return Optional.ofNullable(hashCountOperations.get(biddingItemInfoKey + itemId, bidCountKey));
    }

    public Optional<Integer> getHeartCount(Long itemId) {
        return Optional.ofNullable(hashCountOperations.get(biddingItemInfoKey + itemId, heartCountKey));
    }
}


package site.bidderown.server.bounded_context.item.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@RequiredArgsConstructor
@Repository
public class ItemRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOperations;

    private ZSetOperations<String, String> zSetOperations;

    @Value("${custom.redis.item.bidding.expire-key}")
    private String biddingItemExpireKey;

    @Value("${custom.redis.item.bidding.ranking-key}")
    private String bidRankingKey;

    @PostConstruct
    private void init() {
        valueOperations = redisTemplate.opsForValue();
        zSetOperations = redisTemplate.opsForZSet();
    }

    public void save(Long itemId, int day) {
        valueOperations.set(biddingItemExpireKey + itemId, "", day, TimeUnit.DAYS);
        zSetOperations.add(bidRankingKey, itemId.toString(), 0);
    }

    public boolean contains(Long itemId) {
        return !Objects.isNull(valueOperations.get(biddingItemExpireKey + itemId));
    }

    public void increaseScore(Long itemId, int delta) {
        zSetOperations.incrementScore(bidRankingKey, itemId.toString(), delta);
    }

    public void decreaseScore(Long itemId, int delta) {
        if (delta > 0) throw new RuntimeException("decreaseScore");
        zSetOperations.incrementScore(bidRankingKey, itemId.toString(), delta);
    }

    public void removeBidRankingKey(Long itemId) {
        zSetOperations.remove(bidRankingKey, itemId.toString());
    }
}


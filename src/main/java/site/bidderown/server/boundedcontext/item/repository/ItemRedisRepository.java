package site.bidderown.server.boundedcontext.item.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.ExpiredObjectListener;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import site.bidderown.server.base.event.BidEndEvent;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Repository
public class ItemRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;
    private final ApplicationEventPublisher publisher;

    private ZSetOperations<String, String> zSetOperations;

    @Value("${custom.redis.item.bidding.expire-key}")
    private String biddingItemExpireKey;

    @Value("${custom.redis.item.bidding.ranking-key}")
    private String bidRankingKey;

    @PostConstruct
    private void init() {
        zSetOperations = redisTemplate.opsForZSet();
    }

    public void save(Long itemId, int day) {
        saveItemWithExpire(itemId, day);
        registerBidRanking(itemId);
    }

    private void saveItemWithExpire(Long itemId, int day) {
        RBucket<Object> bucket = redissonClient.getBucket(biddingItemExpireKey + itemId);
        bucket.set("", day, TimeUnit.DAYS);
        bucket.addListener((ExpiredObjectListener) name -> {
            publisher.publishEvent(BidEndEvent.of(itemId));
            log.info("item expired item id: {}", itemId);
        });
    }

    private void registerBidRanking(Long itemId) {
        zSetOperations.add(bidRankingKey, itemId.toString(), 0);
    }

    public boolean contains(Long itemId) {
        return !Objects.isNull(redissonClient.getBucket(biddingItemExpireKey + itemId).get());
    }

    public List<Long> getBidRankingRange(Pageable pageable) {
        long start = pageable.getOffset();
        long end = pageable.getOffset() + pageable.getPageSize() - 1;

        Set<String> ids = zSetOperations.reverseRange(bidRankingKey, start, end);

        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }

        return ids.stream().map(Long::parseLong).collect(Collectors.toList());
    }

    public void increaseScore(Long itemId, int delta) {
        zSetOperations.incrementScore(bidRankingKey, itemId.toString(), delta);
    }

    public void decreaseScore(Long itemId, int delta) {
        zSetOperations.incrementScore(bidRankingKey, itemId.toString(), delta);
    }

    public void removeBidRankingKey(Long itemId) {
        zSetOperations.remove(bidRankingKey, itemId.toString());
    }

    public void flushBidRanking() {
        redisTemplate.delete(bidRankingKey);
    }
}


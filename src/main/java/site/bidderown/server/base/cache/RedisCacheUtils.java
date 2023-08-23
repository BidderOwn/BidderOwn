package site.bidderown.server.base.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
@Component
public class RedisCacheUtils {
    private final RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;

    @PostConstruct
    public void postConstruct() {
        listOperations = redisTemplate.opsForList();
    }



}

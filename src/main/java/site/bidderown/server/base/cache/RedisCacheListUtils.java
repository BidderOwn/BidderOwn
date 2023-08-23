package site.bidderown.server.base.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisCacheListUtils implements CacheListUtils {

    private final RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;

    @PostConstruct
    public void postConstruct() {
        listOperations = redisTemplate.opsForList();
    }


    @Override
    public List<?> get(String key) {
        return listOperations.range(key, 0, -1);
    }

    @Override
    public void set(String key, List<?> objs, long ttl) {
        for (Object obj : objs) {
            listOperations.rightPushAll(key, obj);
        }
        redisTemplate.expire(key, ttl, TimeUnit.MINUTES);
    }

    @Override
    public void add(String key, Object obj, boolean right) {
        if (right) {
            listOperations.rightPush(key, obj);
        } else {
            listOperations.leftPush(key, obj);
        }
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    public void removeKeyPattern(String keyPattern) {
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match(keyPattern + "*").build());
            while (cursor.hasNext()) {
                byte[] key = cursor.next();
                redisTemplate.delete(new String(key));
            }
            cursor.close();
            return null;
        });
    }
}

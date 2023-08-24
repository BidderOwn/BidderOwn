package site.bidderown.server.base.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisCacheListUtils {

    private final RedisTemplate<String, Object> redisTemplate;

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

    public void removeKey(String key) {
        redisTemplate.delete(key);
    }
}

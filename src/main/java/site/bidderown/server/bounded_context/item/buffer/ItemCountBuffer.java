package site.bidderown.server.bounded_context.item.buffer;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.redis.buffer.Buffer;
import site.bidderown.server.base.redis.buffer.BufferTask;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

/**
 * Redis 에 저장되는 Item 과 관련된 엔티티 작업 버퍼
 * 1. Comment, Bid, Heart 엔티티가 생성되면 EntityListener 를 통해서 ItemCountBuffer에 저장된다.
 * 2. ItemCounterScheduler 를 통해 10초마다 버퍼에 있는 작업을 가져온다.
 * 3. Redis 에 있는 item-info key 에 commentCount, bidCount, heartCount에 +1을 한다.
 */

@RequiredArgsConstructor
@Component
public class ItemCountBuffer implements Buffer {

    private final RedisTemplate<String, BufferTask> redisTemplate;
    private ListOperations<String, BufferTask> opsList;

    @Value("${custom.redis.item.bidding.item-count-buffer}")
    private String key;

    @PostConstruct
    public void init() {
        opsList = redisTemplate.opsForList();
    }

    @Override
    public void push(BufferTask bufferTask) {
        opsList.rightPush(key, bufferTask);
    }

    @Override
    public BufferTask pop() {
        return opsList.leftPop(key);
    }

    @Override
    public long size() {
        return Optional.ofNullable(opsList.size(key)).orElse(0L);
    }

    @Override
    public List<BufferTask> popAll() {
        List<BufferTask> tasks = opsList.range(key, 0, -1); // 0 ~ bufferSize
        redisTemplate.delete(key);
        return tasks;
    }
}

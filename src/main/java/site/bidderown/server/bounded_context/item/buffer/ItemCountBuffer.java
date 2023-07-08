package site.bidderown.server.bounded_context.item.buffer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.redis.buffer.Buffer;
import site.bidderown.server.base.redis.buffer.BufferTask;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class ItemCountBuffer implements Buffer {

    private final RedisTemplate<String, BufferTask> redisTemplate;
    private ListOperations<String, BufferTask> opsList;

    private String key = "item-count-buffer";

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
        long bufferSize = size();
        if (bufferSize == 0) return List.of();
        return opsList.leftPop(key, bufferSize);
    }
}

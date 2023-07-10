package site.bidderown.server.bounded_context.heart.entitylistener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.redis.buffer.BufferTask;
import site.bidderown.server.base.redis.buffer.BufferTaskType;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartCountTask implements BufferTask {
    private BufferTaskType type;
    private Long id;

    public static HeartCountTask of(Long itemId) {
        return HeartCountTask.builder()
                .type(BufferTaskType.heart)
                .id(itemId)
                .build();
    }
}
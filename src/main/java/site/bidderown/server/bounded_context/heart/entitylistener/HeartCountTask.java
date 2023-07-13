package site.bidderown.server.bounded_context.heart.entitylistener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.redis.buffer.CountTask;
import site.bidderown.server.base.redis.buffer.CounterTaskType;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartCountTask implements CountTask {
    private CounterTaskType type;
    private Long id;
    private int delta;

    public static HeartCountTask of(Long itemId, int delta) {
        return HeartCountTask.builder()
                .type(CounterTaskType.heart)
                .id(itemId)
                .delta(delta)
                .build();
    }
}
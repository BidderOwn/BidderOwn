package site.bidderown.server.bounded_context.bid.entitylistener;

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
public class BidCountTask implements CountTask {
    private CounterTaskType type;
    private Long id;
    private int delta;

    public static BidCountTask of(Long itemId, int delta) {
        return BidCountTask.builder()
                .type(CounterTaskType.bid)
                .id(itemId)
                .delta(delta)
                .build();
    }
}

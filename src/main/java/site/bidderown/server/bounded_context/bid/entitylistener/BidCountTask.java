package site.bidderown.server.bounded_context.bid.entitylistener;

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
public class BidCountTask implements BufferTask {
    private BufferTaskType type;
    private Long id;

    public static BidCountTask of(Long itemId) {
        return BidCountTask.builder()
                .type(BufferTaskType.bid)
                .id(itemId)
                .build();
    }
}

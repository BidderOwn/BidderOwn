package site.bidderown.server.bounded_context.bid.entitylistener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.redis.buffer.BufferTask;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BidCountTask implements BufferTask {
    private String type;
    private Long id;

    public static BidCountTask of(String type, Long itemId) {
        return BidCountTask.builder()
                .type(type)
                .id(itemId)
                .build();
    }
}

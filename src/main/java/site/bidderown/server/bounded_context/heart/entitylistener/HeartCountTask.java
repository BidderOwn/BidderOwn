package site.bidderown.server.bounded_context.heart.entitylistener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.redis.buffer.BufferTask;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartCountTask implements BufferTask {
    private String type;
    private Long id;

    public static HeartCountTask of(String type, Long itemId) {
        return HeartCountTask.builder()
                .type(type)
                .id(itemId)
                .build();
    }
}
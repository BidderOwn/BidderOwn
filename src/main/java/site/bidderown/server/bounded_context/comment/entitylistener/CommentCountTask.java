package site.bidderown.server.bounded_context.comment.entitylistener;

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
public class CommentCountTask implements CountTask {
    private CounterTaskType type;
    private Long id;
    private int delta;

    public static CommentCountTask of(Long itemId, int delta) {
        return CommentCountTask.builder()
                .type(CounterTaskType.comment)
                .id(itemId)
                .delta(delta)
                .build();
    }
}

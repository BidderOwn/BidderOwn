package site.bidderown.server.bounded_context.comment.entitylistener;

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
public class CommentCountTask implements BufferTask {
    private BufferTaskType type;
    private Long id;

    public static CommentCountTask of(Long itemId) {
        return CommentCountTask.builder()
                .type(BufferTaskType.comment)
                .id(itemId)
                .build();
    }
}

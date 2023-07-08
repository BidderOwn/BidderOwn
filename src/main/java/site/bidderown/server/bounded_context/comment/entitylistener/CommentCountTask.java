package site.bidderown.server.bounded_context.comment.entitylistener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.redis.buffer.BufferTask;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentCountTask implements BufferTask {
    private String type;
    private Long id;

    public static CommentCountTask of(String type, Long itemId) {
        return CommentCountTask.builder()
                .type(type)
                .id(itemId)
                .build();
    }
}

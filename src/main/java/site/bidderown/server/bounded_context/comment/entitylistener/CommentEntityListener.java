package site.bidderown.server.bounded_context.comment.entitylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

/**
 * Comment Entity 추가 이벤트 리스너
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentEntityListener {

    private final ItemCountBuffer itemCountBuffer;

    @Value("${custom.buffer-task.type.comment}")
    private String type;

    @PostPersist
    public void postPersist(Comment comment) {
        log.info("comment post persist {}", comment.getId());
        itemCountBuffer.push(CommentCountTask.of(type, comment.getItem().getId()));
    }
}

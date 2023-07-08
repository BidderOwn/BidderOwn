package site.bidderown.server.bounded_context.comment.entitylistener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;

import javax.persistence.PostPersist;

@Slf4j
@RequiredArgsConstructor
@Component
public class CommentEntityListener {

    private final ItemCountBuffer itemCountBuffer;

    @PostPersist
    public void postPersist(Comment comment) {
        log.info("comment post persist {}", comment.getId());
        itemCountBuffer.push(CommentCountTask.of("comment", comment.getItem().getId()));
    }
}

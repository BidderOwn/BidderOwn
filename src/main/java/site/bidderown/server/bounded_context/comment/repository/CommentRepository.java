package site.bidderown.server.bounded_context.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.comment.entity.Comment;

import org.springframework.data.domain.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findCommentsByItemIdOrderByIdDesc(Long itemId, Pageable pageable);

    List<Comment> findCommentsByCreatedAtAfter(LocalDateTime createdAt);

    Integer countByItemId(Long itemId);
}

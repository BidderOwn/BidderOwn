package site.bidderown.server.bounded_context.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

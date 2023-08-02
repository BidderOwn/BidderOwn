package site.bidderown.server.boundedcontext.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}

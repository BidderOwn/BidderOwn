package site.bidderown.server.bounded_context.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;

import java.util.List;

import static site.bidderown.server.bounded_context.comment.entity.QComment.comment;
import static site.bidderown.server.bounded_context.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<CommentDetailResponse> getComments(Long itemId, Pageable pageable) {
        return queryFactory.select(Projections.constructor(
                        CommentDetailResponse.class,
                        comment.id.as("commentId"),
                        member.id.as("memberId"),
                        member.name,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .join(comment.writer, member)
                .where(member.id.eq(itemId))
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }
}

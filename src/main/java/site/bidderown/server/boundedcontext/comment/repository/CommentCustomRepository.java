package site.bidderown.server.boundedcontext.comment.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.bidderown.server.boundedcontext.comment.controller.dto.CommentDetailResponse;

import java.util.List;

import static site.bidderown.server.boundedcontext.comment.entity.QComment.comment;
import static site.bidderown.server.boundedcontext.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<CommentDetailResponse> getComments(Long itemId, Pageable pageable) {
        return queryFactory.select(Projections.constructor(
                        CommentDetailResponse.class,
                        comment.id,
                        member.id,
                        member.name,
                        comment.content
                ))
                .from(comment)
                .join(comment.writer, member)
                .where(comment.item.id.eq(itemId))
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

    }
}

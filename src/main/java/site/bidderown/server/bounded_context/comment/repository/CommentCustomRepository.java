package site.bidderown.server.bounded_context.comment.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.util.List;
import java.util.stream.Collectors;

import static site.bidderown.server.bounded_context.comment.entity.QComment.comment;
import static site.bidderown.server.bounded_context.item.entity.QItem.item;
import static site.bidderown.server.bounded_context.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Long> findCommentItemIds(String memberName) {
        List<Comment> comments = jpaQueryFactory
                .select(comment)
                .from(comment)
                .join(comment.writer, member)
                .join(comment.item, item)
                .where(
                        eqToMember(memberName),
                        eqToItemStatus()
                )
                .fetch();

        return comments.stream()
                .map(comment -> comment.getItem().getId())
                .collect(Collectors.toList());
    }

    private BooleanExpression eqToMember(String memberName) {
        return member.name.eq(memberName);
    }

    private BooleanExpression eqToItemStatus() {
        return item.itemStatus.eq(ItemStatus.BIDDING);
    }
}

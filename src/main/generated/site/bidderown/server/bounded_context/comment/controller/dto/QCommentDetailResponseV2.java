package site.bidderown.server.bounded_context.comment.controller.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * site.bidderown.server.bounded_context.comment.controller.dto.QCommentDetailResponseV2 is a Querydsl Projection type for CommentDetailResponseV2
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentDetailResponseV2 extends ConstructorExpression<CommentDetailResponseV2> {

    private static final long serialVersionUID = -1469526076L;

    public QCommentDetailResponseV2(com.querydsl.core.types.Expression<Long> commentId, com.querydsl.core.types.Expression<Long> memberId, com.querydsl.core.types.Expression<String> memberName, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(CommentDetailResponseV2.class, new Class<?>[]{long.class, long.class, String.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, commentId, memberId, memberName, content, createdAt, updatedAt);
    }

}


package site.bidderown.server.bounded_context.comment.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDetailResponse {
    private Long commentId;
    private Long memberId;
    private String memberName;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public CommentDetailResponse (
            Long commentId,
            Long memberId,
            String memberName,
            String content,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.commentId = commentId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentDetailResponse of(Comment comment) {
        return CommentDetailResponse.builder()
                .commentId(comment.getId())
                .memberId(comment.getWriter().getId())
                .memberName(comment.getWriter().getName())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }

}

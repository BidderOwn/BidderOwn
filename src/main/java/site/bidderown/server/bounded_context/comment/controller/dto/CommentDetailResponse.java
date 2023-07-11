package site.bidderown.server.bounded_context.comment.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import java.time.LocalDateTime;

@Schema(description = "댓글상세 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentDetailResponse {
    @Schema(description = "댓글 ID")
    private Long commentId;
    @Schema(description = "작성자 ID")
    private Long memberId;
    @Schema(description = "작성자 이름")
    private String memberName;
    @Schema(description = "내용")
    private String content;
    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;
    @Schema(description = "수정 일자")
    private LocalDateTime updatedAt;

    @Builder
    private CommentDetailResponse (
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

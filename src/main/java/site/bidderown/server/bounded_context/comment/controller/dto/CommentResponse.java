package site.bidderown.server.bounded_context.comment.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.member.entity.Member;

@Schema(description = "댓글 응답")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    @Schema(description = "내용")
    private String content;
    @Schema(description = "작성자 이름")
    private String memberName;

    @Builder
    public CommentResponse(
            String content,
            Member writer
    ) {
        this.content = content;
        this.memberName = writer.getName();
    }

    public static CommentResponse of (Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .writer(comment.getWriter())
                .build();
    }

}

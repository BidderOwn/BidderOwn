package site.bidderown.server.bounded_context.comment.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    private String content;
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

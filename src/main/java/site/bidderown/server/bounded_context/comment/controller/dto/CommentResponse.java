package site.bidderown.server.bounded_context.comment.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentResponse {

    private String content;
    private Item item;
    private Member writer;

    @Builder
    public CommentResponse(
            String content,
            Item item,
            Member writer
    ) {
        this.content = content;
        this.item = item;
        this.writer =writer;
    }

    public static CommentResponse of (Comment comment) {
        return CommentResponse.builder()
                .content(comment.getContent())
                .item(comment.getItem())
                .writer(comment.getWriter())
                .build();
    }
}

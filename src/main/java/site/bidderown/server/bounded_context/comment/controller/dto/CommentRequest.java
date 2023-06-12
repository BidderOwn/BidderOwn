package site.bidderown.server.bounded_context.comment.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {

    @NotBlank
    @Length(max = 500)
    private String content;

    @Builder
    public CommentRequest(String content) {
        this.content = content;
    }

    public static CommentRequest of(String content) {
        return CommentRequest.builder()
                .content(content)
                .build();
    }
}

package site.bidderown.server.bounded_context.comment.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.validation.constraints.NotBlank;

@Schema(description = "댓글 요청")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentRequest {
    @Schema(description = "내용", nullable = false, maxLength = 500)
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

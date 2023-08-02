package site.bidderown.server.boundedcontext.comment.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Schema(description = "댓글 요청")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CommentRequest {
    @Schema(description = "내용", nullable = false, maxLength = 500)
    @NotBlank
    @Length(max = 500)
    private String content;

    public static CommentRequest of(String content) {
        return new CommentRequest(content);
    }
}

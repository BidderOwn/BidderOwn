package site.bidderown.server.bounded_context.comment.controller.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class CommentRequest {

    @NotBlank
    @Length(max = 500)
    private String content;

}

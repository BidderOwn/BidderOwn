package site.bidderown.server.bounded_context.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;

@Schema(description = "상품수정 요청")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateRequest {

    @Schema(description = "제목", nullable = false, maxLength = 30)
    @NotBlank
    @Length(max = 30)
    private String title;

    @Schema(description = "설명", nullable = false, maxLength = 500)
    @NotBlank
    @Length(max = 500)
    private String description;

    @Builder
    public ItemUpdateRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static ItemUpdateRequest of(Item item) {
        return ItemUpdateRequest.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .build();
    }
}

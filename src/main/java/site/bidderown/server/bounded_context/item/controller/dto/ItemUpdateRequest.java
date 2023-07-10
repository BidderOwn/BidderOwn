package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateRequest {

    @NotBlank
    @Length(max = 30)
    private String title;

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

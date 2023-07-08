package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

import static site.bidderown.server.bounded_context.item.controller.dto.ItemUpdateRequest.calculateDuration;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateResponse {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;

    @Builder
    public ItemUpdateResponse(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static ItemUpdateResponse of(Item item) {
        return ItemUpdateResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .build();
    }
}

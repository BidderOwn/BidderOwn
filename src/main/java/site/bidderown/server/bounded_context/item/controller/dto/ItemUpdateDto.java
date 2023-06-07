package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateDto extends BaseEntity {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;


    @Builder
    public ItemUpdateDto(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static ItemUpdateDto of(Item item) {
        return ItemUpdateDto.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .build();
    }
}

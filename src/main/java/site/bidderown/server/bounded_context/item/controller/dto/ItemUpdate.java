package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdate extends BaseEntity {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;


    @Builder
    public ItemUpdate(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static ItemUpdate of(Item item) {
        return ItemUpdate.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .build();
    }
}

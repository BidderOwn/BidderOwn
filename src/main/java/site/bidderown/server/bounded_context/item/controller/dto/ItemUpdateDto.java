package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemUpdateDto extends BaseEntity {

    @Column(length = 30)
    @NotBlank
    private String title;

    @NotBlank
    @Column(length = 500)
    private String description;


}

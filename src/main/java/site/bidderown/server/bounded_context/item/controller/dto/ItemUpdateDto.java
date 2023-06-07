package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter @Setter
@AllArgsConstructor
public class ItemUpdateDto extends BaseEntity {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;


}

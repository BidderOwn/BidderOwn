package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.bidderown.server.bounded_context.item.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequest {

    @NotBlank
    @Column(length = 30)
    private String title;

    @NotBlank
    @Column(length = 500)
    private String description;

    @NotBlank
    private int minimumPrice;

    @Size(max = 5)
    private List<Image> images;

    @NotBlank
    private LocalDateTime expireAt;

}

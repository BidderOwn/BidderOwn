package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.bidderown.server.bounded_context.item.entity.Image;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequest {

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    @NotNull
    private int minimumPrice;

//    @OneToMany(mappedBy = "image")
//    private List<Image> imageList;

    private LocalDateTime expireAt;


}

package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class ItemRequest {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotNull
    private Integer minimumPrice;

    // 3,5,7
    @NotNull
    private Integer period;

    private List<MultipartFile> images;

    @NotBlank
    @Length(max = 500)
    private String description;

    @Builder
    public ItemRequest(String title, Integer minimumPrice, Integer period, String description) {
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.period = period;
        this.description = description;
    }
}

package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
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

    private Integer minimumPrice;

    private List<MultipartFile> images;
}

package site.bidderown.server.bounded_context.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Schema(description = "상품 요청")
@Getter @Setter
@NoArgsConstructor
public class ItemRequest {

    @Schema(description = "제목", nullable = false, maxLength = 30)
    @NotBlank
    @Length(max = 30)
    private String title;

    @Schema(description = "최소희망가격", nullable = false, maximum = "1000000000")
    @Max(value = 1000000000, message = "거래 최대금액은 10억입니다.")
    @NotNull
    private Integer minimumPrice;

    // 3,5,7
    @Schema(description = "경매 기간", nullable = false, allowableValues = {"3","5","7"})
    @NotNull
    private Integer period;

    @Schema(description = "사진 리스트", nullable = false)
    @NotNull
    private List<MultipartFile> images;

    @Schema(description = "설명", nullable = false, maxLength = 500)
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

    /**
     * @description 테스트용 생성자입니다.
     */
    public ItemRequest(String title, Integer minimumPrice, Integer period, String description, List<MultipartFile> images) {
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.period = period;
        this.description = description;
        this.images = images;
    }
}

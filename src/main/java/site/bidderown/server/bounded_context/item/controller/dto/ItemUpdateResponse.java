package site.bidderown.server.bounded_context.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

@Schema(description = "상품수정 응답")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateResponse {

    @Schema(description = "제목", nullable = false, maxLength = 30)
    @NotBlank
    @Length(max = 30)
    private String title;

    @Schema(description = "설명", nullable = false, maxLength = 500)
    @NotBlank
    @Length(max = 500)
    private String description;

    @Schema(description = "최소희망가격")
    private int minimumPrice;

    @Schema(description = "경매 기간", allowableValues = {"3","5","7"})
    private int period;

    @Schema(description = "생성 일자")
    private LocalDateTime createdAt;

    @Schema(description = "만료 일자")
    private LocalDateTime expireAt;


    @Builder
    public ItemUpdateResponse(String title, String description, int minimumPrice, LocalDateTime createdAt, LocalDateTime expireAt) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.createdAt = createdAt;
        this.expireAt = expireAt;
        this.period = calculateDuration(createdAt, expireAt);
    }

    public static int calculateDuration(LocalDateTime createdAt, LocalDateTime expireAt) {
        Duration duration = Duration.between(createdAt, expireAt);
        return (int)(duration.toSeconds()/24/60/60)+1;
    }

    public static ItemUpdateResponse of(Item item) {
        return ItemUpdateResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .createdAt(item.getCreatedAt())
                .expireAt(item.getExpireAt())
                .build();
    }
}

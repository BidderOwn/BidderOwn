package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateResponse {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;

    private int minimumPrice;

    private int period;

    private LocalDateTime createdAt;

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

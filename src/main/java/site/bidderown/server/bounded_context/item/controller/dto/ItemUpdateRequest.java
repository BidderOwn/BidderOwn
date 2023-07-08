package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemUpdateRequest {

    @NotBlank
    @Length(max = 30)
    private String title;

    @NotBlank
    @Length(max = 500)
    private String description;

    private int minimumPrice;

//    @NonNull
//    private int period;


    @Builder
    public ItemUpdateRequest(String title, String description, int minimumPrice) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
//        this.period = calculateDuration(createdAt, expireAt);
    }

    public static int calculateDuration(LocalDateTime createdAt, LocalDateTime expireAt) {
        Duration duration = Duration.between(createdAt, expireAt);
        return (int)duration.toSeconds();
    }

    public static ItemUpdateRequest of(Item item) {
//        int period = calculateDuration(item.getCreatedAt(), item.getExpireAt());
        return ItemUpdateRequest.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
//                .period(calculateDuration(item.getCreatedAt(), item.getExpireAt()))
                .build();
    }
}

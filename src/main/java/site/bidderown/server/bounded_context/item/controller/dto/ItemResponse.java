package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;

import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponse {
    private String title;
    private String description;
    private int minimumPrice;
    private String expireAt;
    private Long itemId;
    private String thumbnailImageName;
    @Builder
    public ItemResponse(String title, String description, int minimumPrice, String expireAt, Long itemId, String thumbnailImageName) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.itemId = itemId;
        this.thumbnailImageName = thumbnailImageName;
    }

    public static ItemResponse of(Item item) {
        Duration duration = Duration.between( LocalDateTime.now(), item.getExpireAt());
        String expireAt;
        // 60 1분
        // 60 * 60 1시간  3600
        // 60 * 60 * 24 하루 86400
        long totalSeconds = duration.toSeconds();
        long day = totalSeconds / 86400;
        totalSeconds = totalSeconds % 86400;
        long hour = totalSeconds / 3600;
        totalSeconds = totalSeconds % 3600;
        long minute = totalSeconds / 60;
        long seconds = totalSeconds % 60;



        if(duration.toMinutes() < 1)
        {
            expireAt = seconds + "초";
        } else if (duration.toMinutes() < 60) {
            expireAt = minute + "분 " + seconds + "초";
        } else if (duration.toMinutes() < 1440) {
            expireAt = hour + "시간 " + minute + "분 ";
        }
        else
            expireAt = day  + "일 " + hour + "시간 " + minute + "분 ";

        return ItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(expireAt)
                .itemId(item.getId())
                .thumbnailImageName(item.getThumbnailImage())
                .build();
    }
}


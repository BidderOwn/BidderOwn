package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;

import site.bidderown.server.base.util.TimeUtils;
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
    private String itemStatus;
    @Builder
    public ItemResponse(String title, String description, int minimumPrice, String expireAt, Long itemId, String thumbnailImageName, String itemStatus) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.itemId = itemId;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
    }

    public static ItemResponse of(Item item) {
        String expireAt = TimeUtils.getRemainingTime(LocalDateTime.now(), item.getExpireAt());

        return ItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(expireAt)
                .itemId(item.getId())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus().getStatus())
                .build();
    }
}


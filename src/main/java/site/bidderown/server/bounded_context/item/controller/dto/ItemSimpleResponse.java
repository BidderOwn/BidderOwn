package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;

import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSimpleResponse {
    private String title;
    private int minimumPrice;
    private String expireAt;
    private Long itemId;
    private String thumbnailImageName;
    private String itemStatus;

    @Builder
    public ItemSimpleResponse(String title, int minimumPrice, String expireAt, Long itemId, String thumbnailImageName, String itemStatus) {
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.itemId = itemId;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
    }

    public static ItemSimpleResponse of(Item item) {
        String expireAt = TimeUtils.getRemainingTime(LocalDateTime.now(), item.getExpireAt());

        return ItemSimpleResponse.builder()
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(expireAt)
                .itemId(item.getId())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus().getStatus())
                .build();
    }
}


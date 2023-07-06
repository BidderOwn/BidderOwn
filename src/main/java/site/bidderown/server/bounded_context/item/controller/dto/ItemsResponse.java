package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemsResponse {
    private Long id;
    private String title;
    private int minimumPrice;
    private Integer maxPrice;
    private Integer minPrice;
    private Integer commentsCount;
    private Integer bidCount;
    private String thumbnailImageName;
    private ItemStatus itemStatus;
    private LocalDateTime expireAt;

    @Builder
    public ItemsResponse(
            Long id,
            String title,
            int minimumPrice,
            Integer maxPrice,
            Integer minPrice,
            Integer commentsCount,
            Integer bidCount,
            String thumbnailImageName,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        this.id = id;
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.commentsCount = commentsCount;
        this.bidCount = bidCount;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    @Builder
    public ItemsResponse(
            Long id,
            String title,
            int minimumPrice,
            Integer maxPrice,
            Integer minPrice,
            String thumbnailImageName,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        this.id = id;
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }
}

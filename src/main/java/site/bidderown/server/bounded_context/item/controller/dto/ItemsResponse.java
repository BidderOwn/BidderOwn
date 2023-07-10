package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.dto.ItemCounts;

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
    private Integer heartsCount;
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
        // v1
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

    public static ItemsResponse of__v1(Item item, Integer minPrice, Integer maxPrice) {
        return ItemsResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .commentsCount(item.getComments().size())
                .bidCount(item.getBids().size())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus())
                .expireAt(item.getExpireAt())
                .build();
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

    public void setCounts(ItemCounts itemCounts) {
        this.bidCount = itemCounts.getBidCount();
        this.commentsCount = itemCounts.getCommentCount();
        this.heartsCount = itemCounts.getHeartCount();
    }

}

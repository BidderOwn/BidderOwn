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
            Integer heartsCount,
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
        this.heartsCount = heartsCount;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public ItemsResponse(
            Long id,
            String title,
            int minimumPrice,
            Integer commentsCount,
            Integer bidCount,
            Integer heartsCount,
            String thumbnailImageName,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        // v2에서 사용됨
        this.id = id;
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.commentsCount = commentsCount;
        this.bidCount = bidCount;
        this.heartsCount = heartsCount;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public ItemsResponse(
            Long id,
            String title,
            int minimumPrice,
            String thumbnailImageName,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        // v3에서 사용됨
        this.id = id;
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public static ItemsResponse of__v1(
            Item item,
            Integer minPrice,
            Integer maxPrice
    ) {
        return ItemsResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .commentsCount(item.getComments().size())
                .bidCount(item.getBids().size())
                .heartsCount(item.getHearts().size())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus())
                .expireAt(item.getExpireAt())
                .build();
    }

    public static ItemsResponse of(
            Item item,
            Integer maxPrice,
            Integer minPrice,
            ItemCounts itemCounts
    ) {
        return ItemsResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .commentsCount(itemCounts.getCommentCount())
                .bidCount(itemCounts.getBidCount())
                .heartsCount(itemCounts.getHeartCount())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus())
                .expireAt(item.getExpireAt())
                .build();
    }

    public void setCounts(ItemCounts itemCounts) {
        this.bidCount = itemCounts.getBidCount();
        this.commentsCount = itemCounts.getCommentCount();
        this.heartsCount = itemCounts.getHeartCount();
    }
}

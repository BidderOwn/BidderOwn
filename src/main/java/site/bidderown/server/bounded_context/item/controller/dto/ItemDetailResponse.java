package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.dto.ItemCounts;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetailResponse {
    private Long id;
    private Long sellerId;
    private String title;
    private String description;
    private String memberName;
    private int minimumPrice;
    private Integer maxPrice;
    private Integer minPrice;
    private String thumbnailImageName;
    private int bidCount;
    private int commentCount;
    private int heartCount;
    private ItemStatus itemStatus;
    private LocalDateTime expireAt;

    @Builder
    public ItemDetailResponse(
            Long id,
            Long sellerId,
            String title,
            String description,
            String memberName,
            int minimumPrice,
            Integer maxPrice,
            Integer minPrice,
            String thumbnailImageName,
            int bidCount,
            int commentCount,
            int heartCount,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        this.id = id;
        this.sellerId = sellerId;
        this.title = title;
        this.description = description;
        this.memberName = memberName;
        this.minimumPrice = minimumPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.thumbnailImageName = thumbnailImageName;
        this.bidCount = bidCount;
        this.commentCount = commentCount;
        this.heartCount = heartCount;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public static ItemDetailResponse of(
            Item item,
            Integer maxPrice,
            Integer minPrice,
            ItemCounts itemCounts
    ) {
        return ItemDetailResponse.builder()
                .id(item.getId())
                .sellerId(item.getMember().getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .bidCount(itemCounts.getBidCount())
                .commentCount(itemCounts.getCommentCount())
                .heartCount(itemCounts.getHeartCount())
                .thumbnailImageName(item.getThumbnailImageFileName())
                .bidCount(item.getBids().size())
                .itemStatus(item.getItemStatus())
                .build();
    }

    public static ItemDetailResponse of__v1(
            Item item,
            Integer maxPrice,
            Integer minPrice,
            int bidCount,
            int commentCount,
            int heartCount
    ) {
        return ItemDetailResponse.builder()
                .id(item.getId())
                .sellerId(item.getMember().getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .bidCount(bidCount)
                .commentCount(commentCount)
                .heartCount(heartCount)
                .thumbnailImageName(item.getThumbnailImageFileName())
                .bidCount(item.getBids().size())
                .itemStatus(item.getItemStatus())
                .build();
    }
}

package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

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
    private Integer bidCount;
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
            Integer bidCount,
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
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

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
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public static ItemDetailResponse of(Item item, Integer minPrice, Integer maxPrice) {
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
                .thumbnailImageName(item.getThumbnailImageFileName())
                .bidCount(item.getBids().size())
                .itemStatus(item.getItemStatus())
                .build();
    }
}

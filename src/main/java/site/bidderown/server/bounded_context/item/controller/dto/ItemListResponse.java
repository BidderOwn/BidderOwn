package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemListResponse {
    private Long id;
    String title;
    String memberName;
    int minimumPrice;
    LocalDateTime expireAt;
    Integer maxPrice;
    Integer minPrice;
    int commentsCount;
    int bidCount;
    String thumbnailImageName;
    String itemStatus;

    @Builder
    private ItemListResponse(
            Long id,
            String title,
            String memberName,
            int minimumPrice,
            LocalDateTime expireAt,
            Integer maxPrice,
            Integer minPrice,
            int commentsCount,
            int bidCount,
            String thumbnailImageName,
            String itemStatus
    ) {
        this.id = id;
        this.title = title;
        this.memberName = memberName;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.commentsCount = commentsCount;
        this.bidCount = bidCount;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
    }

    public static ItemListResponse of(Item item, Integer minPrice, Integer maxPrice) {
        return ItemListResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .commentsCount(item.getComments().size())
                .bidCount(item.getBids().size())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus().getStatus())
                .build();
    }
}

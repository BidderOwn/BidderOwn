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
    String title;
    String memberName;
    int minimumPrice;
    LocalDateTime expireAt;
    int maxPrice;
    int minPrice;
    int commentsCount;
    String thumbNailImageName;

    @Builder
    private ItemListResponse (
    String title,
    String memberName,
    int minimumPrice,
    LocalDateTime expireAt,
    int maxPrice,
    int minPrice,
    int commentsCount,
    String thumbNailImageName
    ) {
        this.title = title;
        this.memberName = memberName;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.commentsCount = commentsCount;
        this.thumbNailImageName = thumbNailImageName;
    }

    public static ItemListResponse of (Item item) {
        return ItemListResponse.builder()
                .title(item.getTitle())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .maxPrice(item.getBids().get(0).getPrice())
                .minPrice(item.getBids().get(item.getBids().size()-1).getPrice())
                .commentsCount(item.getComments().size())
                .thumbNailImageName(item.getThumbnailImage())
                .build();
    }
}

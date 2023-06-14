package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetailResponse {
    private Long id;
    private String title;
    private String description;
    private String memberName;
    private int minimumPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expireAt;
    private Integer maxPrice;
    private Integer minPrice;
    private List<Image> images;
    private List<Bid> bids;

    @Builder
    private ItemDetailResponse (
            Long id,
            String title,
            String description,
            String memberName,
            int minimumPrice,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime expireAt,
            Integer minPrice,
            Integer maxPrice,
            List<Image> images,
            List<Bid> bids
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.memberName = memberName;
        this.minimumPrice = minimumPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expireAt = expireAt;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.images = images;
        this.bids = bids;
    }

    public static ItemDetailResponse of (Item item, Integer minPrice, Integer maxPrice) {
        return ItemDetailResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .description(item.getDescription())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .expireAt(item.getExpireAt())
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .images(item.getImages())
                .bids(item.getBids())
                .build();
    }
}

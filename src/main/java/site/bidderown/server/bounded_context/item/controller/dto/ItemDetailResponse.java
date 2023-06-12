package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetailResponse {
    private String title;
    private String description;
    private String memberName;
    private int minimumPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime expireAt;
    private int maxPrice;
    private int minPrice;
    private List<Image> images;
    private List<Comment> comments;

    @Builder
    private ItemDetailResponse (
            String title,
            String description,
            String memberName,
            int minimumPrice,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            LocalDateTime expireAt,
            int maxPrice,
            int minPrice,
            List<Image> images,
            List<Comment> comments
    ) {
        this.title = title;
        this.description = description;
        this.memberName = memberName;
        this.minimumPrice = minimumPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.expireAt = expireAt;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.images = images;
        this.comments = comments;
    }

    public static ItemDetailResponse of (Item item) {
        return ItemDetailResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .memberName(item.getMember().getName())
                .minimumPrice(item.getMinimumPrice())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .expireAt(item.getExpireAt())
                .maxPrice(item.getBids().get(0).getPrice())
                .minPrice(item.getBids().get(item.getBids().size()-1).getPrice())
                .images(item.getImages())
                .comments(item.getComments())
                .build();
    }
}

package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemResponse {
    private String title;
    private String description;
    private int minimumPrice;
    private LocalDateTime expireAt;

    @Builder
    public ItemResponse(String title, String description, int minimumPrice, LocalDateTime expireAt) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
    }

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .build();
    }
}

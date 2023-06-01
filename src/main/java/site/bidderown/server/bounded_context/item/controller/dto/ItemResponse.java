package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
public class ItemResponse {
    private final String title;
    private final String description;
    private final int minimumPrice;
    private final LocalDateTime expireAt;


    public static ItemResponse of(Item item) {
        return ItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .build();
    }
}

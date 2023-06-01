package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import site.bidderown.server.bounded_context.item.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
public class ItemResponse {
    private final String title;
    private final String description;
    private final int minimumPrice;
    private final LocalDateTime expireAt;


    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .title(item.getTitle())
                .description(item.getDescription())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(item.getExpireAt())
                .build();
    }
}

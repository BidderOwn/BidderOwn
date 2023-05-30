package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import site.bidderown.server.bounded_context.item.entity.Image;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@RequiredArgsConstructor
public class ItemResponse {
    private final String title;
    private final String description;
    private final int minimumPrice;
    //private final List<Image> imageList;
    private final LocalDateTime expireAt;

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getTitle(), item.getDescription(), item.getMinimumPrice(), item.getExpireAt());
    }
}

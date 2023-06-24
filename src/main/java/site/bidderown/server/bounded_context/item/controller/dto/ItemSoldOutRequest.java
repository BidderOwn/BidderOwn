package site.bidderown.server.bounded_context.item.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemSoldOutRequest {
    private Long itemId;
}

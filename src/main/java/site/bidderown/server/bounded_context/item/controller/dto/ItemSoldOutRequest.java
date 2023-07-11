package site.bidderown.server.bounded_context.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "판매완료 요청")
@Getter
@Setter
@NoArgsConstructor
public class ItemSoldOutRequest {
    @Schema(description = "상품 ID")
    private Long itemId;
}

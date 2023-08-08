package site.bidderown.server.boundedcontext.bid.controller.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "입찰 요청")
@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidRequest {
    @Schema(description = "상품 ID")
    private Long itemId;
    @Schema(description = "입찰 가격 ")
    private int itemPrice;

    @Builder
    private BidRequest(Long itemId, int itemPrice) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
    }

    public static BidRequest of(Long itemId, int itemPrice){
        return BidRequest.builder()
                .itemId(itemId)
                .itemPrice(itemPrice)
                .build();
    }
}

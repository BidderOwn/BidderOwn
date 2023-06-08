package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidResponse {
    private String bidderName;
    private int price; // 최소 희망 금액
    private int bidPrice; // 입찰 금액
    private Long itemId;
      // img도 들어가야할 것 같습니다.
    //private Long maxPrice;
    //private Long minPrice;
    //private Long avgPrice;


    @Builder
    private BidResponse(String bidderName, int price, int bidPrice, Long itemId) {
        this.bidderName = bidderName;
        this.price = price;
        this.bidPrice = bidPrice;
        this.itemId = itemId;
    }

    public static BidResponse of(Bid bid, Item item){
        return BidResponse.builder()
                .bidderName(bid.getBidder().getName())
                .price(item.getMinimumPrice())
                .bidPrice(bid.getPrice())
                .itemId(item.getId()).build();
    }
}

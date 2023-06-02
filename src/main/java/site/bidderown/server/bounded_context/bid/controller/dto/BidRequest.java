package site.bidderown.server.bounded_context.bid.controller.dto;


import lombok.*;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidRequest {
    private Long itemId;
    private int itemPrice;

    @Builder
    private BidRequest(Long itemId, int itemPrice) {
        this.itemId = itemId;
        this.itemPrice = itemPrice;
    }

    public static BidRequest of(Long itemId, int itemPrice){
        return BidRequest.builder()
                .itemId(itemId)
                .itemPrice(itemPrice).build();
    }
}

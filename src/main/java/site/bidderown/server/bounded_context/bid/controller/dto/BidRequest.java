package site.bidderown.server.bounded_context.bid.controller.dto;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidRequest {
    private Long itemId;
    private int itemPrice;
}

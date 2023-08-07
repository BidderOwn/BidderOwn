package site.bidderown.server.boundedcontext.bid.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.boundedcontext.bid.entity.Bid;

@Schema(description = "입찰 응답")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BidResponse {
    @Schema(description = "입찰자 이름")
    private String bidderName;
    @Schema(description = "입찰금액")
    private int bidPrice;
    @Schema(description = "입찰ID")
    private Long bidId;

    public static BidResponse of(Bid bid){
        return BidResponse.builder()
                .bidderName(bid.getBidder().getName())
                .bidPrice(bid.getPrice())
                .bidId(bid.getId())
                .build();
    }
}

package site.bidderown.server.boundedcontext.bid.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "입찰 응답")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class BidResponses {
    @Schema(description = "입찰 최고가")
    private Integer maxPrice;
    @Schema(description = "입찰 리스트")
    private List<BidResponse> bids;

    public static BidResponses of(Integer maxPrice, List<BidResponse> bids) {
        return BidResponses.builder()
                .maxPrice(maxPrice)
                .bids(bids)
                .build();
    }
}

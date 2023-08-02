package site.bidderown.server.boundedcontext.bid.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(description = "입찰 응답")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidResponses {
    @Schema(description = "입찰 상세")
    private BidDetails bidDetails;
    @Schema(description = "입찰 리스트")
    private List<BidResponse> bids;

    @Builder
    public BidResponses(BidDetails bidDetails, List<BidResponse> bids) {
        this.bidDetails = bidDetails;
        this.bids = bids;
    }

    public static BidResponses of(BidDetails bidDetails, List<BidResponse> bids) {
        return BidResponses.builder()
                .bidDetails(bidDetails)
                .bids(bids)
                .build();
    }
}

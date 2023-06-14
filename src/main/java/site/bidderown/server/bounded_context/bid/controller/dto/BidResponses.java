package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidResponses {
    private BidDetails bidDetails;
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

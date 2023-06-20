package site.bidderown.server.bounded_context.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponses;
import site.bidderown.server.bounded_context.bid.service.BidService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidApiController {

    private final BidService bidService;

    @PostMapping("/api/v1/bid")
    public Long registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        return bidService.handleBid(bidRequest, user.getUsername());
    }

    @GetMapping("/api/v1/bid/list")
    public BidResponses bidListApi(@RequestParam Long itemId){
        BidDetails bidDetails = bidService.getBidStatistics(itemId);
        List<BidResponse> bids = bidService.getBids(itemId);
        return BidResponses.of(bidDetails, bids);
    }

    @GetMapping("/api/v1/bid-ids")
    public List<Long> getBidItemIds(
            @AuthenticationPrincipal User user
    ) {
        return bidService.getBidItemIds(user.getUsername());
    }


}


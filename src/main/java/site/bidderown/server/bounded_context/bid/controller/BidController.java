package site.bidderown.server.bounded_context.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponses;
import site.bidderown.server.bounded_context.bid.service.BidService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping("/bid")
    public String registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        bidService.handleBid(bidRequest, user.getUsername());
        return "redirect:/bid/list?itemId=" + bidRequest.getItemId();
    }

    @PostMapping("/api/v1/bid")
    @ResponseBody
    public Long createBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        return bidService.handleBid(bidRequest, user.getUsername());
    }

    @GetMapping("/bid/list")
    public String bidList(@RequestParam Long itemId, Model model, @AuthenticationPrincipal User user){
        model.addAttribute("itemId", itemId);
        return "usr/bid/list";
    }

    @GetMapping("/api/v1/bid/list")
    @ResponseBody
    public BidResponses bidListApi(@RequestParam Long itemId){
        BidDetails bidDetails = bidService.getBidStatistics(itemId);
        List<BidResponse> bids = bidService.getBids(itemId);
        return BidResponses.of(bidDetails, bids);
    }

    @GetMapping("/api/v1/bid-ids")
    @ResponseBody
    public List<Long> getBidItemIds(
            @AuthenticationPrincipal User user
    ) {
        return bidService.getBidItemIds(user.getUsername());
    }
}

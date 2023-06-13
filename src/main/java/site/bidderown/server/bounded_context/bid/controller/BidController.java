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
    @GetMapping("/bid/list")
    public String bidList(@RequestParam Long itemId, Model model, @AuthenticationPrincipal User user){
        String username = user.getUsername();
        List<BidResponse> bids = bidService.getBids(itemId);
        BidDetails bidDetails = bidService.getBidStatistics(itemId);

        model.addAttribute("bids", bids);
        model.addAttribute("bidDetails", bidDetails);
        model.addAttribute("username", username);
        model.addAttribute("itemId", itemId);
        return "usr/bid/list";
    }

    @GetMapping("/api/v1/bid/list")
    @ResponseBody
    public List<BidResponse> bidListApi(@RequestParam Long itemId){
        return bidService.getBids(itemId);
    }

    @GetMapping("/api/v1/bid-ids")
    @ResponseBody
    public List<Long> getBidItemIds(
            @AuthenticationPrincipal User user
    ) {
        return bidService.getBidItemIds(user.getUsername());
    }
}

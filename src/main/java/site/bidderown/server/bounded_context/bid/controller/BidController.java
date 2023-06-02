package site.bidderown.server.bounded_context.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.service.BidService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BidController {
  
    private final BidService bidService;

    @PostMapping("/bid")
    public String registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        bidService.create(bidRequest, user.getUsername());
        return "usr/item/home";
    }

    @GetMapping("/api/v1/bid/list")
    @ResponseBody
    public List<BidResponse> bidList(@RequestParam Long itemId){
        return bidService.getBids(itemId);
    }
}

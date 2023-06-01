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
@RequestMapping("/bid")
@RequiredArgsConstructor
public class BidController {
  
    private final BidService bidService;

    @PostMapping
    public void registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        bidService.create(bidRequest, user.getUsername());
    }

    @GetMapping("/list")
    public List<BidResponse> bidList(@RequestParam Long itemId){
        return bidService.getBids(itemId);
    }
}

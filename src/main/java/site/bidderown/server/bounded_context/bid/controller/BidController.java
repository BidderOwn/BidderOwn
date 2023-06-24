package site.bidderown.server.bounded_context.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/bid/list")
    @PreAuthorize("isAuthenticated()")
    public String bidList(@RequestParam Long itemId, Model model, @AuthenticationPrincipal User user){
        model.addAttribute("itemId", itemId);
        return "usr/bid/list";
    }
}

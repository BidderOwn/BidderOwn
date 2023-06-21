package site.bidderown.server.bounded_context.bid.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponses;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BidApiController {

    private final BidService bidService;
    private final MemberService memberService;

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
    @DeleteMapping("/api/v1/bid/{bidId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public String deleteBid(@PathVariable Long bidId, @AuthenticationPrincipal User user) {
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(user.getUsername()));

        if (!memberDetail.getName().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        bidService.delete(bidId);
        return "ok";
    }
}


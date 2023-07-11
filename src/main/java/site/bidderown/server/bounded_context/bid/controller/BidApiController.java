package site.bidderown.server.bounded_context.bid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponses;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@Tag(name = "bid", description = "입찰 API")
@RestController
@RequiredArgsConstructor
public class BidApiController {

    private final BidService bidService;
    private final MemberService memberService;

    @Operation(summary = "입찰 생성", description = "")
    @PostMapping("/api/v1/bid")
    public Long registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user){
        if(user == null)
            throw new ForbiddenException("로그인 후 접근이 가능합니다.");
        return bidService.handleBid(bidRequest, user.getUsername());
    }

    @Operation(summary = "입찰 조회", description = "")
    @GetMapping("/api/v1/bid/list")
    public BidResponses bidListApi(@RequestParam Long itemId){
        BidDetails bidDetails = bidService.getBidStatistics(itemId);
        List<BidResponse> bids = bidService.getBids(itemId);
        return BidResponses.of(bidDetails, bids);
    }

    @Operation(summary = "입찰 리스트 조회", description = "")
    @GetMapping("/api/v1/bid-ids")
    public List<Long> getBidItemIds(
            @AuthenticationPrincipal User user
    ) {
        return bidService.getBidItemIds(user.getUsername());
    }

    @Operation(summary = "입찰 삭제", description = "")
    @DeleteMapping("/api/v1/bid/{bidId}")
    @PreAuthorize("isAuthenticated()")
    public void deleteBid(@PathVariable Long bidId, @AuthenticationPrincipal User user) {
        bidService.delete(bidId, user.getUsername());
    }
}


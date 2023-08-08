package site.bidderown.server.boundedcontext.bid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidRequest;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidResponses;
import site.bidderown.server.boundedcontext.bid.service.BidService;

@RequiredArgsConstructor
@Tag(name = "입찰 bid-api-controller", description = "입찰 관련 api 입니다.")
@RestController
@RequestMapping("/api/v1/bid")
public class BidApiController {

    private final BidService bidService;

    @Operation(summary = "입찰 등록", description = "원하는 상품에 입찰가를 제시합니다.")
    @PostMapping
    public void registerBid(@RequestBody BidRequest bidRequest, @AuthenticationPrincipal User user) throws InterruptedException {
        if(user == null) throw new ForbiddenException("로그인 후 접근이 가능합니다.");
        bidService.handleBid(bidRequest, user.getUsername());
    }

    @Operation(summary = "입찰 목록", description = "상품의 id를 통해 입찰 목록을 보여줍니다.")
    @GetMapping("/list")
    public BidResponses bidListApi(@RequestParam Long itemId){
        return bidService.getBidListWithMaxPrice(itemId);
    }

    @Operation(summary = "입찰 취소", description = "입찰을 취소하고 싶을 때 입찰 id와 유저이름을 비교해 취소합니다.")
    @DeleteMapping("/{bidId}")
    @PreAuthorize("isAuthenticated()")
    public void deleteBid(@PathVariable Long bidId, @AuthenticationPrincipal User user) {
        bidService.delete(bidId, user.getUsername());
    }
}


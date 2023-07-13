package site.bidderown.server.bounded_context.heart.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartResponse;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartStatus;
import site.bidderown.server.bounded_context.heart.service.HeartService;

import java.util.List;

@Tag(name = "heart", description = "좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class HeartApiController {

    private final HeartService heartService;

    @Operation(summary = "좋아요 생성/삭제", description = "itemId와 username을 이용하여 좋아요가 없으면 생성하고, 좋아요가 있으면 삭제합니다.")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/likes")
    public HeartResponse handleHeart(@PathVariable("id") Long itemId, @AuthenticationPrincipal User user) {
        return heartService.handleHeart(itemId, user.getUsername());
    }

    @Operation(summary = "좋아요 여부 조회", description = "itemId와 username을 이용하여 사용자가 상품에 좋아요을 했는지 여부를 조회합니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/likeStatus")
    public HeartStatus getLikeStatus(@PathVariable("id") Long itemId, @AuthenticationPrincipal User user) {
        return heartService.getLikeStatus(itemId, user.getUsername());
    }

    @Operation(summary = "좋아요 상품ID 리스트 조회", description = "username을 이용하여 사용자가 좋아요한 상품 리스트를 조회합니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home/likes")
    public List<Long> getLikeIdList(@AuthenticationPrincipal User user) {
        return heartService.getLikeIdList(user.getUsername());
    }
}

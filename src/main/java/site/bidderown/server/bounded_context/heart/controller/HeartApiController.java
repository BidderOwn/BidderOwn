package site.bidderown.server.bounded_context.heart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartResponse;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartStatus;
import site.bidderown.server.bounded_context.heart.service.HeartService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class HeartApiController {

    private final HeartService heartService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/likes")
    public HeartResponse handleHeart(@PathVariable("id") Long itemId, @AuthenticationPrincipal User user) {
        return heartService.handleHeart(itemId, user.getUsername());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/likeStatus")
    public HeartStatus getLikeStatus(@PathVariable("id") Long itemId, @AuthenticationPrincipal User user) {
        return heartService.getLikeStatus(itemId, user.getUsername());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/home/likes")
    public List<Long> getLikeIdList(@AuthenticationPrincipal User user) {
        return heartService.getLikeIdList(user.getUsername());
    }
}

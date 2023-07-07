package site.bidderown.server.bounded_context.heart.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.heart.controller.dto.HeartResponse;
import site.bidderown.server.bounded_context.heart.service.HeartService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class HeartApiController {

    private final HeartService heartService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{id}/likes")
    public HeartResponse clickHeart(@PathVariable("id") Long itemId, @AuthenticationPrincipal User user) {
        return heartService.clickHeart(itemId, user.getUsername());
    }
}

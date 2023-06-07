package site.bidderown.server.bounded_context.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/v1/item/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public CommentResponse createComment(
            @RequestBody CommentRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal Member writer
    ) {
        return CommentResponse.of(commentService.create(request, id, writer));
    }

    @GetMapping("/api/v1/item/{id}")
    @ResponseBody
    public List<CommentDetailResponse> getCommentList(@PathVariable Long id) {
        return commentService.getAll(id);
    }

}

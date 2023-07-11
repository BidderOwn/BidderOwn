package site.bidderown.server.bounded_context.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponseV2;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.service.CommentService;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/item/")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/{itemId}/comment")
    public CommentResponse createComment(
            @RequestBody CommentRequest request,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user
    ) {
        if(user == null)
            throw new ForbiddenException("로그인 후 접근이 가능합니다.");
        return CommentResponse.of(commentService.create(request, itemId, user.getUsername()));
    }

/*    @GetMapping("/{id}/comments")
    public List<CommentDetailResponse> getCommentList(@PathVariable Long id, Pageable pageable) {
        return commentService.getComments(id, pageable);
    }*/

    @GetMapping("/{id}/comments")
    public List<CommentDetailResponseV2> getCommentList(@PathVariable Long id, Pageable pageable) {
        return commentService.getComments(id, pageable);
    }

    @DeleteMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.delete(commentId, user.getUsername());
    }

    @PutMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public CommentResponse updateComment(
            @PathVariable("id") Long commentId,
            @RequestBody @Valid CommentRequest commentRequest,
            @AuthenticationPrincipal User user
    ){
        return commentService.update(commentId, commentRequest, user.getUsername());
    }
}

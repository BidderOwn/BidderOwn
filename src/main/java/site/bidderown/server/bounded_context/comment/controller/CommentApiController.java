package site.bidderown.server.bounded_context.comment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "comment", description = "댓글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/item/")
public class CommentApiController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "commentRequest, itemId, username을 이용하여 comment를 생성합니다.")
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

    @Operation(summary = "댓글 리스트 조회", description = "itemId를 이용하여 comment 리스트를 조회합니다.")
    @GetMapping("/{id}/comments")
    public List<CommentDetailResponse> getCommentList(@PathVariable Long id) {
        return commentService.getComments(id);
    }

    @Operation(summary = "댓글 삭제", description = "commentId와 username을 이용하여 comment를 삭제합니다.")
    @DeleteMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.delete(commentId, user.getUsername());
    }

    @Operation(summary = "댓글 수정", description = "commentId, commentRequest, username을 이용하여 comment를 수정합니다.")
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

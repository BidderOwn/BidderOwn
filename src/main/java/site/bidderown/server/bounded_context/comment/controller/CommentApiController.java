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
import org.springframework.data.domain.Pageable;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "댓글 comment-api-controller", description = "댓글 관련 api 입니다.")
@RequestMapping("/api/v1/item/")
public class CommentApiController {
    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "상품에 대한 댓글을 작성합니다.")
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

    @Operation(summary = "댓글 목록", description = "상품에 대한 댓글 목록을 조회합니다.")
    @GetMapping("/{id}/comments")
    public List<CommentDetailResponse> getCommentList(@PathVariable Long id, Pageable pageable) {
        return commentService.getComments(id, pageable);
    }

    @Operation(summary = "댓글 삭제", description = "상품에 대한 댓글을 삭제합니다.")
    @DeleteMapping("/{id}/comment")
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(
            @PathVariable("id") Long commentId,
            @AuthenticationPrincipal User user
    ) {
        commentService.delete(commentId, user.getUsername());
    }

    @Operation(summary = "댓글 수정", description = "상품에 대한 댓글을 삭제합니다.")
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

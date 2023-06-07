package site.bidderown.server.bounded_context.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class CommentController {
    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping("/api/v1/item/{id}")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public CommentResponse createComment(
            @RequestBody CommentRequest request,
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        return CommentResponse.of(commentService.create(request, id, user.getUsername()));
    }

    @GetMapping("/api/v1/item/{id}")
    @ResponseBody
    public List<CommentDetailResponse> getCommentList(@PathVariable Long id) {
        return commentService.getAll(id);
    }

    @DeleteMapping("/api/v1/itme/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteComment(Long commentId, Principal principal) {
        Comment comment = commentService.getComment(commentId);
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(comment.getWriter().getId()));

        if (!memberDetail.getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        commentService.delete(commentId);
    }

}

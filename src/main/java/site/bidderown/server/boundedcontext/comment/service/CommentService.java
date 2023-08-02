package site.bidderown.server.boundedcontext.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.boundedcontext.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.boundedcontext.comment.controller.dto.CommentRequest;
import site.bidderown.server.boundedcontext.comment.controller.dto.CommentResponse;
import site.bidderown.server.boundedcontext.comment.entity.Comment;
import site.bidderown.server.boundedcontext.comment.repository.CommentCustomRepository;
import site.bidderown.server.boundedcontext.comment.repository.CommentRepository;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentCustomRepository commentCustomRepository;
    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final MemberService memberService;

    @Transactional
    public Comment create(CommentRequest request, Long itemId, String writerName) {
        Item item = itemService.getItem(itemId);
        Member writer = memberService.getMember(writerName);
        Comment comment = Comment.of(request.getContent(), item, writer);
        return commentRepository.save(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("댓글이 존재하지 않습니다.", commentId + ""));
    }

    public List<CommentDetailResponse> getComments(Long itemId, Pageable pageable) {
        return commentCustomRepository.getComments(itemId, pageable);
    }

    @Transactional
    public Long delete(Long commentId, String memberName) {
        Comment comment = getComment(commentId);

        if (!hasAuthorization(comment, memberName)) {
            throw new ForbiddenException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
        return commentId;
    }

    public CommentResponse update(Long commentId, CommentRequest commentRequest, String memberName) {
        Comment comment = getComment(commentId);

        if (!hasAuthorization(comment, memberName)) {
            throw new ForbiddenException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(commentRequest.getContent());
        return CommentResponse.of(comment);
    }

    private boolean hasAuthorization(Comment comment, String memberName) {
        return comment.getWriter().getName().equals(memberName);
    }
}

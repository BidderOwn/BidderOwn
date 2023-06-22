package site.bidderown.server.bounded_context.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemSellerNotification;
import site.bidderown.server.base.exception.ForbiddenException;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ItemService itemService;
    private final MemberService memberService;
    private final ApplicationEventPublisher publisher;

    public Comment create(CommentRequest request, Long itemId, String writerName) {
        Item item = itemService.getItem(itemId);
        Member writer = memberService.getMember(writerName);
        Comment comment = Comment.of(request, item, writer);

        publisher.publishEvent(EventItemSellerNotification.of(item));

        return commentRepository.save(comment);
    }

    public Comment create(CommentRequest request, Long itemId, Member writer) {
        Item item = itemService.getItem(itemId);
        Comment comment = Comment.of(request, item, writer);

        publisher.publishEvent(EventItemSellerNotification.of(item));

        return commentRepository.save(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId));
    }

    public List<CommentDetailResponse> getComments(Long itemId) {
        return commentRepository
                .findAllByItemId(itemId)
                .stream()
                .map(CommentDetailResponse::of)
                .collect(Collectors.toList());
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

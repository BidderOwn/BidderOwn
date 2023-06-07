package site.bidderown.server.bounded_context.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentResponse;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdateDto;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
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

    public Comment create(CommentRequest request, Long itemId, String writerName) {
        Item item = itemService.getItem(itemId);
        Member writer = memberService.getMember(writerName);
        Comment comment = Comment.of(request, item, writer);
        return commentRepository.save(comment);
    }

    public Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId));
    }
    public List<CommentDetailResponse> getAll(Long itemId) {
        return commentRepository
                .findAllByItemId(itemId)
                .stream()
                .map(CommentDetailResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId));
        commentRepository.delete(comment);
    }


    public CommentResponse updateById(Long commentId, CommentRequest commentRequest, String name) {
        Comment findComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(commentId));

        findComment.update(commentRequest);

        return new CommentResponse(findComment.getContent(), findComment.getItem(), findComment.getWriter());
    }
}

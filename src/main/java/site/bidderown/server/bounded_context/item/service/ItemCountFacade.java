package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;
import site.bidderown.server.bounded_context.heart.repository.HeartRepository;

@RequiredArgsConstructor
@Component
public class ItemCountFacade {

    private final BidRepository bidRepository;
    private final CommentRepository commentRepository;
    private final HeartRepository heartRepository;

    public int getBidCount(Long itemId) {
        return bidRepository.countByItemId(itemId);
    }

    public int getCommentCount(Long itemId) {
        return commentRepository.countByItemId(itemId);
    }

    public int getHeartCount(Long itemId) {
        return heartRepository.countByItemId(itemId);
    }
}

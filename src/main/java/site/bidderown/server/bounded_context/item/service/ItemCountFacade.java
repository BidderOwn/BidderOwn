package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;

@RequiredArgsConstructor
@Component
public class ItemCountFacade {

    private final ItemRedisService itemRedisService;
    private final BidRepository bidRepository;
    private final CommentRepository commentRepository;

    public int getBidCount(Long itemId) {
        return itemRedisService.getBidCount(itemId)
                .orElseGet(() -> bidRepository.countByItemId(itemId));
    }

    public int getCommentCount(Long itemId) {
        return itemRedisService.getCommentCount(itemId)
                .orElseGet(() -> commentRepository.countByItemId(itemId));
    }
}

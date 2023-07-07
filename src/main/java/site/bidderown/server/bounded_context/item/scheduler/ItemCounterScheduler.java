package site.bidderown.server.bounded_context.item.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.scheduler.Scheduler;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemCounterScheduler implements Scheduler {
    private final ItemRedisRepository itemRedisRepository;
    private final ItemRedisService itemRedisService;
    private final CommentService commentService;
    private final BidService bidService;

    @Override
    public void run() {
        LocalDateTime updatedAt = itemRedisRepository.getBiddingItemUpdatedAt();
        countBid(updatedAt);
        countComment(updatedAt);
        itemRedisRepository.updateBiddingItemUpdatedAt();
    }

    public void countBid(LocalDateTime updatedAt) {
        List<Long> itemIds = bidService.getBidsAfter(updatedAt)
                .stream()
                .map(bid -> bid.getItem().getId())
                .toList();
        itemRedisService.increaseBidCount(itemIds);
    }

    public void countComment(LocalDateTime updatedAt) {
        List<Long> itemIds = commentService.getCommentsAfter(updatedAt)
                .stream()
                .map(comment -> comment.getItem().getId())
                .toList();
        itemRedisService.increaseCommentCount(itemIds);
    }
}

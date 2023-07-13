package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.base.redis.buffer.CountTask;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.repository.dto.ItemCounts;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemRedisService {

    private final ItemRedisRepository itemRedisRepository;
    private final ItemRepository itemRepository;

    public boolean containsKey(Item item) {
        return itemRedisRepository.contains(item.getId());
    }

    public void createWithExpire(Item item, int expire) {
        itemRedisRepository.save(item.getId(), expire);
    }

    /**
     * item 의 count 정보를 한번에 가져오는 메서드
     */
    public ItemCounts getItemCounts(Item item) {
        return itemRedisRepository.getItemCounts(item.getId())
                .orElseGet(() -> ItemCounts.of(
                        item.getBids().size(),
                        item.getComments().size(),
                        item.getHearts().size()
                ));
    }

    public ItemCounts getItemCounts(Long itemId) {
        return itemRedisRepository.getItemCounts(itemId)
                .orElseGet(() -> {
                    Item item = itemRepository.findById(itemId)
                            .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다.", itemId + ""));
                    return ItemCounts.of(
                            item.getBids().size(),
                            item.getComments().size(),
                            item.getHearts().size()
                    );
                });
    }

    /**
     * Redis 의 pipelining 을 사용하여서 효율적으로 증가
     */
    public void handleTasks(List<CountTask> tasks) {
        itemRedisRepository.handleTasksWithPipelined(tasks);
    }
}

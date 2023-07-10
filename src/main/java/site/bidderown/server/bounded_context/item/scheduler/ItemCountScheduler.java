package site.bidderown.server.bounded_context.item.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.scheduler.Scheduler;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;

/**
 *  Item 의 Bid, Comment, Heart 의 개수를 주기적으로 집계하기 위한 클래스
 *  SchedulerRunner 를 통해 실행된다.
 */

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemCountScheduler implements Scheduler {

    private final ItemCountBuffer itemCountBuffer;
    private final ItemRedisService itemRedisService;

    /**
     * 현재 itemCountBuffer 에 있는 작업을 가져와서 한번에 처리합니다.
     */
    @Override
    public void run() {
        itemCountBuffer.popAll()
                .forEach(task -> itemRedisService.increaseCount(task.getId(), task.getType().toString()));
    }
}

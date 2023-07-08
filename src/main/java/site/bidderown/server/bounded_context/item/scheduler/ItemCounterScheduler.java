package site.bidderown.server.bounded_context.item.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.scheduler.Scheduler;
import site.bidderown.server.bounded_context.item.buffer.ItemCountBuffer;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;

@Slf4j
@RequiredArgsConstructor
@Component
public class ItemCounterScheduler implements Scheduler {

    private final ItemCountBuffer itemCountBuffer;
    private final ItemRedisService itemRedisService;

    @Override
    public void run() {
        itemCountBuffer.popAll()
                .forEach(task ->
                        itemRedisService.increaseCount(task.getId(), task.getType())
                );
    }
}

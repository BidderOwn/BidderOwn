package site.bidderown.server.base.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.item.scheduler.ItemCounterScheduler;

@RequiredArgsConstructor
@Component
@EnableScheduling
public class SchedulerRunner {

    private final ItemCounterScheduler itemCounterScheduler;

    /**
     * 30초마다 아이템 댓글, 입찰 개수 업데이트
     */
    @Scheduled(cron = "0/30 * * * * *")
    public void itemCounterRun() {
        itemCounterScheduler.run();
    }
}

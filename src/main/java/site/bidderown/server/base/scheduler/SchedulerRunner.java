package site.bidderown.server.base.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bidderown.server.bounded_context.item.scheduler.ItemCountScheduler;

/**
 * Scheduler 를 실행시키는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class SchedulerRunner {

    private final ItemCountScheduler itemCountScheduler;

    /**
     * 5초마다 아이템 댓글, 입찰 개수 업데이트
     */
    @Scheduled(cron = "0/5 * * * * *")
    public void itemCounterRun() {
        log.info("itemCounterScheduler run");
        itemCountScheduler.run();
    }
}

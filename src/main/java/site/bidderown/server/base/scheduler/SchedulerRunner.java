package site.bidderown.server.base.scheduler;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bidderown.server.boundedcontext.item.service.ItemService;

/**
 * Scheduler 를 실행시키는 클래스
 */
@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class SchedulerRunner {

    private final ItemService itemService;

    @Scheduled(cron = "0 * * * * *")
    public void bidEndProcessor()
    {
        log.info("bidEndProcessor run");
        itemService.expireBidEndItems();
    }

}
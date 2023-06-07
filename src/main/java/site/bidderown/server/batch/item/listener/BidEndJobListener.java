package site.bidderown.server.batch.item.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;

@Slf4j
public class BidEndJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info(
                "데이터 처리 건수 : {}",
                jobExecution.getStepExecutions()
                        .stream()
                        .mapToInt(StepExecution::getWriteCount)
                        .sum()
        );

    }
}

package site.bidderown.server.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bidderown.server.batch.item.config.ItemJobConfiguration;

@RequiredArgsConstructor
@Component
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final ItemJobConfiguration itemJobConfiguration;
    private final CommandLineRunner initData;

    @Scheduled(cron = "10 * * * * *")
    public void bidEndScheduler() throws Exception {
        jobLauncher.run(
                itemJobConfiguration.bidEndJob(initData),
                new JobParametersBuilder().addString("testKey", "testValue").toJobParameters());
    }
}

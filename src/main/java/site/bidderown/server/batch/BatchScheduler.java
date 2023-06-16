package site.bidderown.server.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import site.bidderown.server.batch.item.config.ItemJobConfiguration;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class BatchScheduler {

    private final JobLauncher jobLauncher;
    private final ItemJobConfiguration itemJobConfiguration;
    private final CommandLineRunner initData;

    @Scheduled(cron = "0 0 * * * *")
    public void bidEndScheduler() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        log.info("bidEndScheduler");
        jobLauncher.run(
                itemJobConfiguration.bidEndJob(initData),
                jobParameters);
    }
}

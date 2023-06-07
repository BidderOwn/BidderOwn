package site.bidderown.server.batch.hello.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import site.bidderown.server.batch.hello.step.HelloStepTasklet;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class HelloConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final HelloStepTasklet helloStepTasklet;

    @Bean
    public Job helloJob() {
        return jobBuilderFactory.get("helloJob")
                .incrementer(new RunIdIncrementer())
                .start(helloStep())
                .build();

    }

    private Step helloStep() {
        return stepBuilderFactory.get("helloStep")
                .tasklet(helloStepTasklet)
                .build();
    }
}

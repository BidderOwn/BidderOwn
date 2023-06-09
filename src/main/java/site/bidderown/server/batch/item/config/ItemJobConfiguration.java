package site.bidderown.server.batch.item.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import site.bidderown.server.batch.item.listener.BidEndItemWriterListener;
import site.bidderown.server.batch.item.listener.BidEndJobListener;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final ApplicationEventPublisher publisher;
    private final ItemRepository itemRepository;
    private final TaskExecutor taskExecutor;
    
    private final int CHUNK_SIZE = 1000;

    @Bean
    public Job bidEndJob(CommandLineRunner initData) throws Exception{
        initData.run();
        return jobBuilderFactory.get("bidEndJob")
                .incrementer(new RunIdIncrementer())
                .start(bidEndStep())
                .listener(new BidEndJobListener())
                .build();
    }

    @Bean
    @JobScope
    public Step bidEndStep() throws Exception {
        return stepBuilderFactory.get("bidEndStep")
                .<Item, Item>chunk(CHUNK_SIZE)
                .reader(bidEndStepItemReader())
//                .processor(bidEndStepJpaItemProcessor())
//                .writer(bidEndStepJpaItemWriter())
                .writer(jPQLItemWriter())
                .listener(new BidEndItemWriterListener(publisher))
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }

    @StepScope
    public ItemReader<Item> bidEndStepItemReader() throws Exception {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(),
                        now.getHour(), 0, 0);
        LocalDateTime endDateTime = startDateTime.plusHours(1);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDateTime", startDateTime);
        parameters.put("endDateTime", endDateTime);

        JpaPagingItemReader<Item> itemReader = new JpaPagingItemReaderBuilder<Item>()
                .name("bidEndStepItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString(
                        "SELECT i " +
                                "FROM Item i " +
                                "WHERE i.createdAt >= :startDateTime AND i.createdAt < :endDateTime")
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .saveState(false)
                .build();

        itemReader.afterPropertiesSet();

        return itemReader;
    }

    @StepScope
    public ItemProcessor<Item, Item> bidEndStepJpaItemProcessor(){
        return item -> {
            item.updateStatus(ItemStatus.BID_END);
            return item;
        };
    }

    @StepScope
    public JpaItemWriter<Item> bidEndStepJpaItemWriter() throws Exception {

        JpaItemWriter<Item> itemWriter = new JpaItemWriterBuilder<Item>()
                .entityManagerFactory(entityManagerFactory)
                .build();

        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @StepScope
    public ItemWriter<Item> jPQLItemWriter() {
        return items -> {
            List<Long> ids = items.stream()
                    .map(Item::getId)
                    .toList();

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDateTime = LocalDateTime
                    .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), now.getHour(), 0, 0);
            LocalDateTime endDateTime = startDateTime.plusHours(1);

            itemRepository.updateItemStatus(ItemStatus.BID_END, startDateTime, endDateTime, ids);
        };
    }
}

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
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.batch.item.listener.BidEndNotificationWriterListener;
import site.bidderown.server.batch.item.listener.BidEndJobListener;
import site.bidderown.server.batch.item.step.reader.BidEndNotificationStepItemReader;
import site.bidderown.server.batch.item.step.writer.BidEndNotificationStepItemWriter;
import site.bidderown.server.bounded_context.bid.repository.BidCustomRepository;
import site.bidderown.server.bounded_context.bid.repository.dto.BidEndNotification;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;

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
    private final BidCustomRepository bidCustomRepository;
    private final NotificationJdbcRepository notificationJdbcRepository;

    private final int CHUNK_SIZE = 1000;

    @Bean
    public Job bidEndJob(CommandLineRunner initData) throws Exception{
        initData.run();
        return jobBuilderFactory.get("bidEndJob")
                .incrementer(new RunIdIncrementer())
                .start(bidEndStep())
                .next(bidEndNotificationStep())
                .listener(new BidEndJobListener())
                .build();
    }

    @JobScope
    public Step bidEndStep() throws Exception {
        return stepBuilderFactory.get("bidEndStep")
                .<Item, Item>chunk(CHUNK_SIZE)
                .reader(bidEndStepItemReader())
                .writer(jPQLItemWriter())
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }


    @JobScope
    public Step bidEndNotificationStep() {
        return stepBuilderFactory.get("bidEndNotificationStep")
                .<BidEndNotification, BidEndNotification>chunk(CHUNK_SIZE)
                .reader(new BidEndNotificationStepItemReader(bidCustomRepository, CHUNK_SIZE))
                .writer(new BidEndNotificationStepItemWriter(notificationJdbcRepository))
                .listener(new BidEndNotificationWriterListener(publisher))
                .build();
    }

    @StepScope
    public ItemReader<Item> bidEndStepItemReader() throws Exception {
        LocalDateTime startDateTime = TimeUtils.getCurrentOClock();
        LocalDateTime endDateTime = TimeUtils.getCurrentOClockPlus(1);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDateTime", startDateTime);
        parameters.put("endDateTime", endDateTime);

        JpaPagingItemReader<Item> itemReader = new JpaPagingItemReaderBuilder<Item>()
                .name("bidEndStepItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString( // TODO expireAt으로 변경
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
        log.info("bidEndStepJpaItemWriter()");
        JpaItemWriter<Item> itemWriter = new JpaItemWriterBuilder<Item>()
                .entityManagerFactory(entityManagerFactory)
                .build();

        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    @StepScope
    public ItemWriter<Item> jPQLItemWriter() {
        log.info("jPQLItemWriter");
        return items -> {
            List<Long> ids = items.stream()
                    .map(Item::getId)
                    .toList();

            LocalDateTime startDateTime = TimeUtils.getCurrentOClock();
            LocalDateTime endDateTime = TimeUtils.getCurrentOClockPlus(1);
            itemRepository.updateItemStatus(ItemStatus.BID_END, startDateTime, endDateTime, ids);
        };
    }
}

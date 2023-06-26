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
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.batch.item.listener.BidEndJobListener;
import site.bidderown.server.batch.item.listener.BidEndNotificationWriterListener;
import site.bidderown.server.batch.item.listener.BidEndWriterListener;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.notification.controller.dto.BulkInsertNotification;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;
import site.bidderown.server.bounded_context.notification.repository.dto.JdbcNotification;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    private final NotificationService notificationService;
    private final NotificationJdbcRepository notificationJdbcRepository;
    private final int CHUNK_SIZE = 5000;

    @Bean
    public Job bidEndJob(CommandLineRunner initData) throws Exception {
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
                .listener(new BidEndWriterListener(publisher))
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }


    @JobScope
    public Step bidEndNotificationJdbcStep() throws Exception {
        return stepBuilderFactory.get("bidEndNotificationStep")
                .<Bid, JdbcNotification>chunk(CHUNK_SIZE)
                .reader(bidEndNotificationStepItemReader())
                .processor(bidEndNotificationJdbcStepItemProcessor())
                .writer(jdbcBatchNotificationWriter())
                .listener(new BidEndNotificationWriterListener(publisher))
                .build();
    }

    @JobScope
    public Step bidEndNotificationStep() throws Exception {
        return stepBuilderFactory.get("bidEndNotificationStep")
                .<Bid, Notification>chunk(CHUNK_SIZE)
                .reader(bidEndNotificationStepItemReader())
                .processor(bidEndNotificationStepItemProcessor())
                .writer(bidEndNotificationStepItemWriter())
                .listener(new BidEndNotificationWriterListener(publisher))
                .taskExecutor(taskExecutor)
                .throttleLimit(8)
                .build();
    }

    @StepScope
    public ItemReader<Item> bidEndStepItemReader() throws Exception {
        LocalDateTime startDateTime = TimeUtils.getCurrentOClock();
        LocalDateTime endDateTime = TimeUtils.getCurrentOClockPlus(1);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", startDateTime);
        parameters.put("end", endDateTime);
        parameters.put("itemStatus", ItemStatus.BIDDING);

        JpaPagingItemReader<Item> itemReader = new JpaPagingItemReaderBuilder<Item>()
                .name("bidEndStepItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString( // TODO expireAt으로 변경
                        "SELECT i " +
                        "FROM Item i " +
                        "WHERE i.createdAt >= :start AND i.createdAt < :end " +
                        "AND  i.itemStatus = :itemStatus")
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .saveState(false)
                .build();

        itemReader.afterPropertiesSet();

        return itemReader;
    }

    @StepScope
    public ItemReader<Bid> bidEndNotificationStepItemReader() throws Exception {
        LocalDateTime startDateTime = TimeUtils.getCurrentOClock();
        LocalDateTime endDateTime = TimeUtils.getCurrentOClockPlus(1);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", startDateTime);
        parameters.put("end", endDateTime);
        parameters.put("itemStatus", ItemStatus.BID_END);

        JpaPagingItemReader<Bid> itemReader = new JpaPagingItemReaderBuilder<Bid>()
                .name("bidEndStepItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString( // TODO expireAt으로 변경
                        "SELECT b " +
                        "FROM Bid b " +
                        "JOIN  b.item i " +
                        "WHERE i.createdAt >= :start AND i.createdAt < :end " +
                        "AND  i.itemStatus = :itemStatus"
                )
                .parameterValues(parameters)
                .pageSize(CHUNK_SIZE)
                .saveState(false)
                .build();

        itemReader.afterPropertiesSet();

        return itemReader;
    }

    @StepScope
    public ItemProcessor<Bid, JdbcNotification> bidEndNotificationJdbcStepItemProcessor() {
        return bid -> JdbcNotification.of(
                bid.getItem().getId(),
                bid.getBidder().getId(),
                NotificationType.BID_END.toString());
    }

    @StepScope
    public ItemWriter<JdbcNotification> jdbcBatchNotificationWriter() {
        return items -> {
            List<BulkInsertNotification> bulkInsertNotifications = new ArrayList<>();
            items.forEach(notification -> bulkInsertNotifications.add(
                    BulkInsertNotification.of(
                            notification.getItemId(),
                            notification.getReceiverId(),
                            NotificationType.BID_END)
            ));

            notificationJdbcRepository.insertNotificationList(bulkInsertNotifications);
        };
    }

    @StepScope
    public ItemProcessor<Bid, Notification> bidEndNotificationStepItemProcessor() {
        return bid -> Notification.of(
                bid.getItem(),
                bid.getBidder(),
                NotificationType.BID_END
        );
    }

    @StepScope
    public ItemWriter<? super Notification> bidEndNotificationStepItemWriter() {
        return notifications -> {
            notificationService.create(notifications.stream()
                    .map(notification -> Notification.of(
                            notification.getItem(),
                            notification.getReceiver(),
                            NotificationType.BID_END))
                    .collect(Collectors.toList()));
        };
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

package site.bidderown.server.batch.item.step.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import site.bidderown.server.bounded_context.bid.repository.dto.BidEndNotification;
import site.bidderown.server.bounded_context.notification.controller.dto.BulkInsertNotification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;

import java.util.List;

@Slf4j
public class BidEndNotificationStepItemWriter implements ItemWriter<BidEndNotification> {
    private final NotificationJdbcRepository notificationJdbcRepository;
    public BidEndNotificationStepItemWriter(NotificationJdbcRepository notificationJdbcRepository) {
        this.notificationJdbcRepository = notificationJdbcRepository;
    }

    @Override
    public void write(List<? extends BidEndNotification> items) throws Exception {
        List<BulkInsertNotification> notifications = items.stream()
                .map(item -> BulkInsertNotification.of(
                        item.getItemId(),
                        item.getReceiverId(),
                        NotificationType.BID_END))
                .toList();
        notificationJdbcRepository.insertNotificationList(notifications);
    }
}

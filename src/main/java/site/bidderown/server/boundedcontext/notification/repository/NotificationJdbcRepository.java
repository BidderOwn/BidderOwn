package site.bidderown.server.boundedcontext.notification.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.boundedcontext.notification.controller.dto.BulkInsertNotification;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


@Repository
@RequiredArgsConstructor
@Slf4j
public class NotificationJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void insertNotificationList(List<BulkInsertNotification> notificationList){
        jdbcTemplate.batchUpdate("insert into notification (created_at, updated_at, notification_type, read_date, item_id, receiver_id) values ( ?, ?, ?, ?, ?, ?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setTimestamp(1, java.sql.Timestamp.valueOf(notificationList.get(i).getCreatedDate()));
                        ps.setTimestamp(2, java.sql.Timestamp.valueOf(notificationList.get(i).getUpdatedDate()));
                        ps.setString(3, notificationList.get(i).getNotificationType());
                        ps.setTimestamp(4, null);
                        ps.setLong(5, notificationList.get(i).getItemId());
                        ps.setLong(6, notificationList.get(i).getReceiverId());
                    }

                    @Override
                    public int getBatchSize() {
                        return notificationList.size();
                    }
                });
    }
}
package site.bidderown.server.bounded_context.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReceiverAndReadDateIsNullOrderByCreatedAtDesc(Member receiver);
    List<Notification> findByReadDateIsNull();
    Integer countByReceiverAndReadDateIsNull(Member receiver);

    Boolean existsByReceiverAndReadDateIsNull(Member receiver);
}

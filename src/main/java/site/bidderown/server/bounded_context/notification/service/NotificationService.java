package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemNotification;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.repository.NotificationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;

    public List<Notification> list(String username) {
        Member member = memberService.getMember(username);
        return notificationRepository.findByReceiverAndReadDateIsNullOrderByCreatedAtDesc(member);
    }

    @Transactional
    public void create(EventItemNotification eventItemNotification) {
        notificationRepository.save(Notification.of(
                eventItemNotification.getItem(),
                eventItemNotification.getReceiver(),
                eventItemNotification.getType()
        ));
    }

    public void create(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void readAll() {
        List<Notification> notifications = notificationRepository.findByReadDateIsNull();
        notifications.stream().forEach(notification -> notification.read());
    }
}

package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemCommentNotification;
import site.bidderown.server.base.event.EventItemBidNotification;
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

    public List<Notification> getNotifications(String username) {
        Member member = memberService.getMember(username);
        return notificationRepository.findByReceiverAndReadDateIsNullOrderByCreatedAtDesc(member);
    }

    @Transactional
    public void create(EventItemBidNotification eventItemBidNotification) {
        notificationRepository.save(Notification.of(
                eventItemBidNotification.getItem(),
                eventItemBidNotification.getReceiver(),
                eventItemBidNotification.getType()
        ));
    }

    @Transactional
    public void create(EventItemCommentNotification eventItemCommentNotification) {
        notificationRepository.save(Notification.of(
                eventItemCommentNotification.getItem(),
                eventItemCommentNotification.getItem().getMember(),
                eventItemCommentNotification.getType()
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

    public boolean checkNotRead(String memberName) {
        Member member = memberService.getMember(memberName);
        return notificationRepository.countByReceiverAndReadDateIsNull(member) > 0;
    }

    public void createNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
}

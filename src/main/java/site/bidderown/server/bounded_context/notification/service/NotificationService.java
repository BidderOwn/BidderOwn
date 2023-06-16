package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemSellerNotification;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.base.event.EventSoldOutNotification;
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
    public void create(EventItemBidderNotification eventItemBidderNotification) {
        notificationRepository.save(Notification.of(
                eventItemBidderNotification.getItem(),
                eventItemBidderNotification.getBidder(),
                eventItemBidderNotification.getType()
        ));
    }

    @Transactional
    public void create(EventItemSellerNotification eventItemSellerNotification) {
        notificationRepository.save(Notification.of(
                eventItemSellerNotification.getItem(),
                eventItemSellerNotification.getItem().getMember(),
                eventItemSellerNotification.getType()
        ));
    }

    public void create(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void readAll() {
        List<Notification> notifications = notificationRepository.findByReadDateIsNull();
        notifications.stream().forEach(Notification::read);
    }

    public boolean checkNotRead(String memberName) {
        Member member = memberService.getMember(memberName);
        return notificationRepository.existsByReceiverAndReadDateIsNull(member);
    }

    public void createNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
}

package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.controller.dto.NewBidNotificationRequest;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${custom.socket.path}")
    private String socketPath;

    @Value("${custom.socket.alarm_type_notification}")
    private String ALARM_TYPE;

    public List<Notification> getNotifications(String username) {
        Member member = memberService.getMember(username);
        return notificationRepository.findByReceiverAndReadDateIsNullOrderByCreatedAtDesc(member);
    }

    public void create(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }
    public Notification create(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    public void readAll(String username) {
        List<Notification> notifications = notificationRepository.findByReceiverNameAndReadDateIsNull(username);
        notifications.forEach(Notification::read);
    }

    public boolean checkNotRead(String memberName) {
        Member member = memberService.getMember(memberName);
        return notificationRepository.existsByReceiverAndReadDateIsNull(member);
    }

    public List<Notification> createNotifications(List<Notification> notifications) {
        return notificationRepository.saveAll(notifications);
    }

    @Transactional
    public List<Notification> createNewBidNotification(NewBidNotificationRequest request) {
        Item item = itemService.getItem(request.getItemId());
        List<Notification> notifications = new ArrayList<>();

        item.getBids().stream()
                .filter(bid -> !bid.getBidder().getName().equals(request.getMemberName()))
                .forEach(bid -> {
                    sendNotification(bid.getBidder().getId());
                    notifications.add(Notification.of(item, bid.getBidder(), NotificationType.BID));
                });

        notifications.add(Notification.of(item, item.getMember(), NotificationType.BID));
        sendNotification(item.getMember().getId());

        return createNotifications(notifications);
    }
    @Transactional
    public Notification createNewCommentNotification(Long itemId, String writerName) {
        Item item = itemService.getItem(itemId);

        if(item.getMember().getName().equals(writerName))
            return null;

        messagingTemplate.convertAndSend(socketPath + item.getMember().getId(), ALARM_TYPE);
        return notificationRepository.save(Notification.of(item, item.getMember(), NotificationType.COMMENT));
    }

    @Transactional
    public List<Notification> createSoldOutNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        List<Notification> notifications = new ArrayList<>();

        item.getBids().forEach(bid -> {
                    sendNotification(bid.getBidder().getId());
                    notifications.add(Notification.of(item, bid.getBidder(), NotificationType.SOLDOUT));
                });

        return createNotifications(notifications);
    }
    public void sendNotification(Long receiverId){
        messagingTemplate.convertAndSend(socketPath + receiverId, ALARM_TYPE);
    }
}

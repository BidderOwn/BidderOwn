package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.controller.dto.NewBidNotificationRequest;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationRepository;

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

    @Transactional
    public void readAll(String username) {
        List<Notification> notifications = notificationRepository.findByReceiverNameAndReadDateIsNull(username);
        notifications.stream().forEach(Notification::read);
    }

    public boolean checkNotRead(String memberName) {
        Member member = memberService.getMember(memberName);
        return notificationRepository.existsByReceiverAndReadDateIsNull(member);
    }

    public void createNotifications(List<Notification> notifications) {
        notificationRepository.saveAll(notifications);
    }

    @Transactional
    public void createNewBidNotification(NewBidNotificationRequest request) {
        Item item = itemService.getItem(request.getItemId());
        List<Notification> notifications = item.getBids().stream()
                .filter(bid -> !bid.getBidder().getName().equals(request.getMemberName()))
                .map(bid -> Notification.of(item, bid.getBidder(), NotificationType.BID))
                .collect(Collectors.toList());
        notifications.add(Notification.of(item, item.getMember(), NotificationType.BID));
        createNotifications(notifications);
        noticeNewBid(item, request.getMemberName());
    }

    public void noticeNewBid(Item item, String memberName) {
        item.getBids().stream()
                .filter(bid -> !bid.getBidder().getName().equals(memberName))
                .map(bid -> bid.getBidder().getId())
                .forEach(receiveId -> messagingTemplate.convertAndSend(socketPath + receiveId, ALARM_TYPE));

        messagingTemplate.convertAndSend(socketPath + item.getMember().getId(), ALARM_TYPE);
    }

    @Transactional
    public void createNewCommentNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        notificationRepository.save(Notification.of(item, item.getMember(), NotificationType.COMMENT));
        noticeNewComment(item.getMember().getId());
    }

    private void noticeNewComment(Long sellerId) {
        messagingTemplate.convertAndSend(socketPath + sellerId, ALARM_TYPE);
    }

    @Transactional
    public void createSoldOutNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        List<Notification> notifications = item.getBids().stream()
                .map(bid -> Notification.of(item, bid.getBidder(), NotificationType.SOLDOUT))
                .collect(Collectors.toList());
        createNotifications(notifications);
        noticeSoldOut(item);
    }

    public void noticeSoldOut(Item item) {
        item.getBids().stream()
                .map(bid -> bid.getBidder().getId())
                .forEach(receiveId -> messagingTemplate.convertAndSend(socketPath + receiveId, ALARM_TYPE));
    }
}

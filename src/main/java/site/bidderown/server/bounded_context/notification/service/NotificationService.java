package site.bidderown.server.bounded_context.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemSellerNotification;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
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

    @Transactional
    public Notification create(EventItemBidderNotification eventItemBidderNotification) {
        return notificationRepository.save(Notification.of(
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

    public void createNewBidNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        List<Notification> notifications = item.getBids().stream()
//                .filter(bid -> !bid.getBidder().getName().equals(memberName))
                .map(bid -> Notification.of(item, bid.getBidder(), NotificationType.BID))
                .collect(Collectors.toList());
        createNotifications(notifications);
        noticeNewBid(item);
    }

    private void noticeNewBid(Item item) {
        item.getBids().stream()
                .map(Bid::getId)
                .forEach(receiveId -> messagingTemplate.convertAndSend(socketPath + receiveId, ALARM_TYPE));
        messagingTemplate.convertAndSend(socketPath + item.getMember().getId(), ALARM_TYPE);
    }

    public void createNewCommentNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        notificationRepository.save(Notification.of(item, item.getMember(), NotificationType.SOLDOUT));

        noticeNewComment(item.getMember().getId());
    }

    private void noticeNewComment(Long sellerId) {
        messagingTemplate.convertAndSend(socketPath + sellerId, ALARM_TYPE);
    }



    public void createSoldOutNotification(Long itemId) {
        Item item = itemService.getItem(itemId);
        List<Notification> notifications = item.getBids().stream()
                .map(bid -> Notification.of(item, bid.getBidder(), NotificationType.SOLDOUT))
                .collect(Collectors.toList());
        createNotifications(notifications);
        noticeSoldOut(item);
    }

    private void noticeSoldOut(Item item) {
        item.getBids().stream()
                .map(Bid::getId)
                .forEach(receiveId -> messagingTemplate.convertAndSend(socketPath + receiveId, ALARM_TYPE));
    }
}

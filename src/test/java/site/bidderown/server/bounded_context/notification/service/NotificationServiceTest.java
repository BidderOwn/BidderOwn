package site.bidderown.server.bounded_context.notification.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.repository.NotificationRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceTest {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ItemService itemService;
    @Autowired
    private MemberService memberService;

    @BeforeEach
    public void beforeEach(){
        notificationRepository.deleteAll();
    }



    @Rollback(value = false)
    @Test
    @DisplayName("입찰 알림 등록")
    void t01(){
        /**
         * 알림을 등록한 뒤 알림의 DB에서 조회해 온 findNotification의 멤버값들을 각각 비교합니다.
         */

        Item item = itemService.getItem(1L);
        Member member = memberService.getMember(1L);

        Notification notification = notificationService.create(EventItemBidderNotification.of(item, member));

        Notification findNotification = notificationRepository.findById(notification.getId()).orElse(null);

        Assertions.assertEquals(notification.getItem() , findNotification.getItem());
        Assertions.assertEquals(notification.getReceiver() , findNotification.getReceiver());
        Assertions.assertEquals(notification.getNotificationType() , findNotification.getNotificationType());
    }

    @Rollback(value = false)
    @Test
    @DisplayName("알림 전체 조회")
    void t02(){
        /**
         *  알림을 2개 등록한 뒤 모든 알림을 조회하여 개수를 비교합니다.
         */
        Item item1 = itemService.getItem(1L);
        Item item2 = itemService.getItem(2L);
        Member member = memberService.getMember(1L);

        Notification notification1 = notificationService.create(EventItemBidderNotification.of(item1, member));
        Notification notification2 = notificationService.create(EventItemBidderNotification.of(item2, member));
        List<Notification> notifications = notificationService.getNotifications(member.getName());

        Assertions.assertEquals(notifications.size(), 2);
    }

    @Rollback(value = false)
    @Test
    @DisplayName("알림 전체 읽음 처리")
    void t03(){
        /**
         * 알림을 2개 등록한 뒤 모든 알림을 읽음처리합니다.
         * 알림의 readDate가 notNull이라면 모든 알림을 읽었음을 의미합니다.
         */
        Item item1 = itemService.getItem(1L);
        Item item2 = itemService.getItem(2L);
        Member member = memberService.getMember(1L);

        Notification notification1 = notificationService.create(EventItemBidderNotification.of(item1, member));
        Notification notification2 = notificationService.create(EventItemBidderNotification.of(item2, member));

        notificationService.readAll();

        Assertions.assertNotNull(notification1.getReadDate());
        Assertions.assertNotNull(notification2.getReadDate());
    }

    @Rollback(value = false)
    @Test
    @DisplayName("알림 확인 여부 체크")
    void t04(){
        /**
         * 알림을 2개 등록한 뒤 모든 알림을 읽음처리합니다.
         * 알림의 readDate가 notNull이라면 모든 알림을 읽었음을 의미합니다.
         */
        Item item1 = itemService.getItem(1L);
        Member member = memberService.getMember(1L);

        Notification notification1 = notificationService.create(EventItemBidderNotification.of(item1, member));

        Assertions.assertEquals(notificationService.checkNotRead(member.getName()), true);

        notificationService.readAll();

        Assertions.assertEquals(notificationService.checkNotRead(member.getName()), false);
    }

}
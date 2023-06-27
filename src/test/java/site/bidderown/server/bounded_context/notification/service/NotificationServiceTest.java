package site.bidderown.server.bounded_context.notification.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.controller.dto.NewBidNotificationRequest;
import site.bidderown.server.bounded_context.notification.entity.Notification;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private BidService bidService;



    Member createUser(String username){
        return memberService.join(username,"1234");
    }
    Item createItem(Member member, String itemTitle, String itemDescription, Integer minimumPrice){
        Item item = itemRepository.save(
                Item.of(
                    ItemRequest.builder()
                    .title(itemTitle)
                    .description(itemDescription)
                    .period(3)
                    .minimumPrice(minimumPrice)
                    .build(), member));
        imageService.create(item, List.of("image1.jpeg"));
        return item;
    }

    @Test
    @DisplayName("알림 개별 등록")
    void t01(){
        /**
         * 알림을 등록한 뒤 알림의 DB에서 조회해 온 findNotification의 멤버값들을 각각 비교합니다.
         */
        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        //when
        Notification notification = notificationService.create(Notification.of(item, seller, NotificationType.BID));
        Notification findNotification = notificationRepository.findById(notification.getId()).orElse(null);

        //then
        Assertions.assertEquals(notification.getItem() , findNotification.getItem());
        Assertions.assertEquals(notification.getReceiver() , findNotification.getReceiver());
        Assertions.assertEquals(notification.getNotificationType() , findNotification.getNotificationType());
    }

    @Test
    @DisplayName("알림 등록 일괄처리")
    void t02(){
        /**
         * 알림을 등록을 일괄 처리한 뒤 DB에서 조회해 온 알림 목록의 사이즈를 비교해봅니다.
         */
        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);


        //when
        List<Notification> notifications = new ArrayList<>();

        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.BID)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.BID_END)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.SOLDOUT)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.COMMENT)));
        notificationService.create(notifications);

        List<Notification> findNotifications = notificationService.getNotifications(seller.getName());

        //then
        assertThat(findNotifications.size()).isEqualTo(4);

    }

    @Test
    @DisplayName("자신의 알림 모두 읽음처리")
    void t03(){
        /**
         *  자신의 알림들만 모두 읽음처리 합니다.
         *  모든 알림에서 자신의 알림의 수 만큼만 줄어들게 됩니다.
         *  모든 알림의 수 == 읽은 후 모든 알림의 수 - 읽은 양
         */
        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        //when
        List<Notification> notifications = new ArrayList<>();

        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.BID)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.BID_END)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.SOLDOUT)));
        notifications.add(notificationService.create(Notification.of(item, seller, NotificationType.COMMENT)));
        notificationService.create(notifications);

        int before = notificationRepository.findAllByReadDateIsNull().size();
        int readAmount = notificationService.getNotifications(seller.getName()).size();

        notificationService.readAll(seller.getName());


        //then
        assertThat(notificationRepository.findAllByReadDateIsNull().size()).isEqualTo(before - readAmount);
    }
    @Test
    @DisplayName("알림 읽음, 읽지않음 체크")
    void t04(){
        /**
         *  member의 읽음 여부에 따라 true, false값을 리턴하는 checkNotRead의 동작 테스트를 진행합니다.
         *  읽기 전 true
         *  읽은 후 false
         */
        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        Notification notification = notificationService.create(Notification.of(item, seller, NotificationType.BID));
        assertThat(notificationService.checkNotRead(seller.getName())).isTrue();

        //when
        notification.read();


        //then
        assertThat(notificationService.checkNotRead(seller.getName())).isFalse();
    }

    @Test
    @DisplayName("입찰 등록 시 알림 발행")
    void t05(){
        /**
         *
         *  아이템에는 bidder1의 입찰이 등록 돼 있는 상황이다.
         *  bidder2가 아이템에 입찰 등록을 했을 때 알림을 받는 사람은 판매자와 bidder1 뿐이다.
         *  본인 제외 입찰자 + 판매자!
         */

        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);
        Member bidder1 = createUser("member2");
        Member bidder2 = createUser("member3");
        bidService.handleBid(BidRequest.of(item.getId(), 10000), bidder1.getName());

        //when
        List<Notification> newBidNotifications = notificationService.createNewBidNotification(NewBidNotificationRequest.of(item.getId(), bidder2.getName()));

        List<String> receivers = newBidNotifications.stream().map(notification -> notification.getReceiver().getName())
                .collect(Collectors.toList());

        //then

        assertThat(newBidNotifications.get(0).getNotificationType()).isEqualTo(NotificationType.BID);
        assertThat(newBidNotifications.get(0).getItem()).isEqualTo(item);
        assertThat(seller.getName()).isIn(receivers);
        assertThat(bidder1.getName()).isIn(receivers);
        assertThat(bidder2.getName()).isNotIn(receivers);

    }

    @Test
    @DisplayName("댓글 등록 시 알림 발행")
    void t06(){
        /**
         *
         *  판매자는 아이템에 댓글이 달렸을 때 알림을 받습니다.
         */

        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);
        Member buyer = createUser("member2");

        //when
        Notification newCommentNotification = notificationService.createNewCommentNotification(item.getId());

        //then
        assertThat(newCommentNotification.getReceiver().getName()).isEqualTo("member1");
        assertThat(newCommentNotification.getNotificationType()).isEqualTo(NotificationType.COMMENT);

    }

    @Test
    @DisplayName("판매 완료 처리 시 알림 발행")
    void t07(){
        /**
         *
         *  판매자가 상품의 상태값을 판매 완료로 변경했을 때
         *  모든 입찰자들은 알림을 받습니다.
         */
        //given
        Member seller = createUser("member1");
        Item item = createItem(seller, "item1", "itemDescription", 10000);
        Member bidder1 = createUser("member2");
        Member bidder2 = createUser("member3");
        bidService.handleBid(BidRequest.of(item.getId(), 10000), bidder1.getName());
        bidService.handleBid(BidRequest.of(item.getId(), 20000), bidder2.getName());

        //when
        List<Notification> soldOutNotifications = notificationService.createSoldOutNotification(item.getId());
        List<String> receivers = soldOutNotifications.stream().map(notification -> notification.getReceiver().getName())
                .collect(Collectors.toList());

        //then
        assertThat(soldOutNotifications.get(0).getNotificationType()).isEqualTo(NotificationType.SOLDOUT);
        assertThat(soldOutNotifications.get(0).getItem()).isEqualTo(item);

        assertThat(bidder1.getName()).isIn(receivers);
        assertThat(bidder2.getName()).isIn(receivers);

    }
}
package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.base.event.EventItemNotification;
import site.bidderown.server.bounded_context.bid.controller.dto.BulkInsertBid;
import site.bidderown.server.bounded_context.bid.repository.BidJdbcRepository;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.item.controller.dto.BulkInsertItem;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemJdbcRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.notification.repository.NotificationJdbcRepository;
import site.bidderown.server.bounded_context.notification.service.NotificationService;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Profile({"dev"})
@Configuration
public class NotProd {
    private boolean initDataDone = false;
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ItemJdbcRepository itemJdbcRepository,
            BidJdbcRepository bidJdbcRepository,
            ItemRepository itemRepository,
            BidRepository bidRepository,
            NotificationService notificationService,
            NotificationJdbcRepository notificationJdbcRepository,
            ItemService itemService,
            CommentService commentService
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            // 유저 생성
            IntStream.rangeClosed(1, 10)
                    .forEach(i -> memberService.loginAsSocial("user_" + i));
            Member member1 = memberService.getMember("user_1");
            Member member2 = memberService.getMember("user_2");
            Member kakaoMember1 = memberService.loginAsSocial("KAKAO_2810203532");
            Member kakaoMember2 = memberService.loginAsSocial("KAKAO_2829157954");

            long startTime = System.currentTimeMillis();

            BulkInsertItem item;
            // 아이템 등록
            long n = 99;
            for (long i = 1; i <= 1; i++){
                ArrayList<BulkInsertItem> itemList = new ArrayList<>();
                ArrayList<BulkInsertBid> bidList = new ArrayList<>();
                for(long j = 1; j <= 100; j++) {
                    if (j % 2 == 0) {
                        itemList.add(BulkInsertItem.builder()
                                .memberId(member1.getId())
                                .title("testTitle_" + j)
                                .description("testDescription")
                                .minimumPrice(10000)
                                .build());
                    } else {
                        itemList.add(BulkInsertItem.builder()
                                .memberId(member2.getId())
                                .title("testTitle_" + j)
                                .description("testDescription")
                                .minimumPrice(10000)
                                .build());
                    }
                    bidList.add(BulkInsertBid.builder()
                            .price(10000)
                            .bidderId(member2.getId())
                            .itemId(n)
                            .build());

                }
                n++;
                itemJdbcRepository.insertItemList(itemList);
                bidJdbcRepository.insertBidList(bidList);
                commentService.create(CommentRequest.of("테스트 댓글1"), 100L, "user_1");
                commentService.create(CommentRequest.of("테스트 댓글2"), 100L, "user_1");
                commentService.create(CommentRequest.of("테스트 댓글3"), 100L, "user_1");
                commentService.create(CommentRequest.of("테스트 댓글4"), 100L, "user_1");
                commentService.create(CommentRequest.of("테스트 댓글5"), 100L, "user_1");

            }
            Item item1 = itemService.getItem(1L);
            for (int i = 0; i <= 10; i++)
                notificationService.create(EventItemNotification.of(item1, kakaoMember1, NotificationType.BID));
            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Init Data: %20dms", endTime - startTime));

            /*  테스트를 별도로 작성할까 하다가 일단 여기에서 진행했습니다. 한번 실행 해보셔도 좋고 그냥 지워주셔도 됩니다!

            Item item = Item.builder().minimumPrice(10)
                    .title("t")
                    .description("t")
                    .minimumPrice(10)
                    .member(member1).build();
            itemRepository.save(item);

            long startTime = System.currentTimeMillis();

            ArrayList<Notification> notifications = new ArrayList<>();
            for(int i = 1; i <= 20; i++) {
                for (int j = 1; j < 10000; j++) {
                    notifications.add(Notification.builder().receiver(member1).build());
                }
                notificationService.create(notifications);
            }


           ArrayList<BulkInsertNotification> notifications = new ArrayList<>();
            for(int i = 1; i <= 20; i++) {
                for (int j = 1; j < 10000; j++) {
                    notifications.add(BulkInsertNotification.builder()
                                    .receiverId(member1.getId())
                                    .itemId(item.getId()).
                            build());
                }
                notificationJdbcRepository.insertNotificationList(notifications);
            }


            long endTime = System.currentTimeMillis();
            System.out.println(String.format("Init Data: %20dms", endTime - startTime));
            */

        };
    }
}
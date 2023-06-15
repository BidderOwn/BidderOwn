package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.bounded_context.bid.controller.dto.BulkInsertBid;
import site.bidderown.server.bounded_context.bid.repository.BidJdbcRepository;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.BulkInsertItem;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
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
import java.util.Arrays;
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
            CommentService commentService,
            ImageService imageService
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            // 유저 생성
            IntStream.rangeClosed(1, 10)
                    .forEach(i -> memberService.loginAsSocial("user_" + i));
            Member member1 = memberService.join("user1", "1234");
            Member member2 = memberService.join("user2", "1234");
            Member kakaoMember1 = memberService.loginAsSocial("KAKAO_2810203532");
            Member kakaoMember2 = memberService.loginAsSocial("KAKAO_2829157954");
            Member kakaoMember3 = memberService.loginAsSocial("KAKAO_2829504082");

            long startTime = System.currentTimeMillis();

            /**
             *  기존의 생성 방식은 아이템 서비스를 통해 이미지 파일을 저장한 뒤 추출된 파일명으로 이미지 엔티티를 생성 후 저장합니다.
             *  임시로 데이터를 넣기위해서는 이미지 파일 저장 로직을 생략한 뒤 아이템만 저장합니다.
             *  이후 images/item에 존재하는 파일명으로 이미지 엔티티를 생성한 뒤 연관관계를 설정해줍니다.
             */
            Item item = Item.of(ItemRequest.builder().
                    title("바나나")
                    .description("description")
                    .period(3)
                    .minimumPrice(10000)
                    .build(), member1);
            itemRepository.save(item);

            imageService.create(item, Arrays.asList("banana.jpg"));


            Item item1 = itemService.getItem(1L);
            for (int i = 0; i <= 10; i++)
                notificationService.create(EventItemBidderNotification.of(item1, kakaoMember1));
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
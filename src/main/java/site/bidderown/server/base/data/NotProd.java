package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.stream.IntStream;

@Profile({"dev"})
@Configuration
public class NotProd {
    private boolean initDataDone = false;
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChatRoomService chatRoomService,
            ItemService itemService,
            ItemRepository itemRepository,
            BidService bidService
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

            Item item;
            for (int i = 1; i <= 1000; i++){
                if (i % 2 == 0) {
                    item = itemRepository.save(Item.builder()
                            .title("item_" + i)
                            .description("testDescription")
                            .minimumPrice(10000)
                            .member(member1).build());
                } else {
                    item = itemRepository.save(Item.builder()
                            .title("item_" + i)
                            .description("testDescription")
                            .minimumPrice(10000)
                            .member(member2).build());
                }
                bidService.create(
                        BidRequest.builder()
                                .itemId(item.getId())
                                .itemPrice(10000)
                                .build(),
                        "user_1" );
            }
        };
    }
}

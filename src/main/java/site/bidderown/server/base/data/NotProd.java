package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@Profile({"dev"})
@Configuration
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChatRoomService chatRoomService,
            ItemService itemService
    ) {
        return args -> {
            // 유저 생성
            IntStream.rangeClosed(1, 10)
                    .forEach(i -> memberService.loginAsSocial("user_" + i));
            Member member1 = memberService.getMember("user_1");
            Member kakaoMember1 = memberService.loginAsSocial("KAKAO_2810203532");

            // 아이템 등록
//            Item item1 = itemService.create(
//                    new ItemRequest("title1", "desc1", 10000, LocalDateTime.now()),
//                    member1.getId()
//            );

//            ChatRoom chatRoom1 = chatRoomService.create(
//                    ChatRoomRequest.of(member1.getId(), kakaoMember1.getId(), item1.getId()));
        };
    }
}

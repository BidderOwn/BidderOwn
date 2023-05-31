package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.stream.IntStream;

@Profile({"dev"})
@Configuration
public class NotProd {
    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ChatRoomService chatRoomService
    ) {
        return args -> {
            IntStream.range(0, 10)
                    .forEach(i -> memberService.join(i + ""));
        };
    }
}

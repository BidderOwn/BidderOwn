package site.bidderown.server.bounded_context.chat_room.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("채팅방 생성 테스트")
    void t001() {
        //given

        Member seller = memberService.findByName("0");
        Member buyer = memberService.findByName("1");

        // when
        ChatRoom savedChatRoom = chatRoomService.create(
                ChatRoomRequest.from(seller.getId(), buyer.getId()));

        //then
        assertNotNull(savedChatRoom);
        assertTrue(chatRoomService.findAllByMemberId(seller.getId()).size() > 0);
        assertEquals(seller.getId(), savedChatRoom.getSeller().getId());
        assertEquals(buyer.getId(), savedChatRoom.getBuyer().getId());
    }
}
package site.bidderown.server.bounded_context.chat_room.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChatRoomServiceTest {

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private MemberService memberService;

    @AfterEach
    void initData() {
        chatRoomService.clear();
    }

    @Test
    @DisplayName("채팅방 생성 테스트")
    void t001() {
        //given
        Member seller = memberService.findByName("user_0");
        Member buyer = memberService.findByName("user_1");

        // when
        ChatRoom savedChatRoom = chatRoomService.create(
                ChatRoomRequest.from(seller.getId(), buyer.getId(), 0L));

        //then
        assertNotNull(savedChatRoom);
        assertTrue(chatRoomService.findAllByMemberId(seller.getId()).size() > 0);
        assertEquals(seller.getId(), savedChatRoom.getSeller().getId());
        assertEquals(buyer.getId(), savedChatRoom.getBuyer().getId());
    }

    @Test
    @DisplayName("member0이 속한 모든 채팅방 가져오기 테스트")
    void t002() {
        //given
        Member member0 = memberService.findByName("user_0");
        Member member1 = memberService.findByName("user_1");
        Member member2 = memberService.findByName("user_2");
        Member member3 = memberService.findByName("user_3");
        Member member4 = memberService.findByName("user_4");

        //member0가 seller
        chatRoomService.create(ChatRoomRequest.from(member0.getId(), member1.getId(), 0L));
        chatRoomService.create(ChatRoomRequest.from(member0.getId(), member2.getId(), 0L));

        //member0가 buyer
        chatRoomService.create(ChatRoomRequest.from(member3.getId(), member0.getId(), 0L));
        chatRoomService.create(ChatRoomRequest.from(member1.getId(), member0.getId(), 0L));
        chatRoomService.create(ChatRoomRequest.from(member4.getId(), member0.getId(), 0L));

        //when
        List<ChatRoomResponse> chatRooms = chatRoomService.findAllByMemberId(member0.getId());

        //then
        assertEquals(5, chatRooms.size());

        long buyerCount = chatRooms.stream()
                .filter(chatRoom -> chatRoom.getToName().equals(member0.getName()))
                .count();

        assertEquals(0, buyerCount);
    }

    @Test
    @DisplayName("채팅방 조회 테스트")
    void t003() {
        //given
        Member seller = memberService.findByName("user_0");
        Member buyer = memberService.findByName("user_1");

        // when
        ChatRoom saveChatRoom = chatRoomService.create(
                ChatRoomRequest.from(seller.getId(), buyer.getId(), 0L));


        ChatRoom findChatRoom = chatRoomService.findById(saveChatRoom.getId());
        //then
        assertEquals(saveChatRoom.getId() ,findChatRoom.getId());
    }
}
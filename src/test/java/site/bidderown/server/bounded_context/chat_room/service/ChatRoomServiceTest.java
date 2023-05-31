package site.bidderown.server.bounded_context.chat_room.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomRepository;
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

    @Test
    @DisplayName("member0이 속한 모든 채팅방 가져오기 테스트")
    void t002() {
        //given
        Member member0 = memberService.findByName("0");
        Member member1 = memberService.findByName("1");
        Member member2 = memberService.findByName("2");
        Member member3 = memberService.findByName("3");
        Member member4 = memberService.findByName("4");

        //member0가 seller
        chatRoomService.create(ChatRoomRequest.from(member0.getId(), member1.getId()));
        chatRoomService.create(ChatRoomRequest.from(member0.getId(), member2.getId()));

        //member0가 buyer
        chatRoomService.create(ChatRoomRequest.from(member3.getId(), member0.getId()));
        chatRoomService.create(ChatRoomRequest.from(member1.getId(), member0.getId()));
        chatRoomService.create(ChatRoomRequest.from(member4.getId(), member0.getId()));

        //when
        List<ChatRoomResponse> chatRooms = chatRoomService.findAllByMemberId(member0.getId());

        //then
        assertEquals(5, chatRooms.size());

        long buyerCount = chatRooms.stream()
                .filter(chatRoom -> chatRoom.getToName().equals(member0.getName()))
                .count();

        assertEquals(0, buyerCount);
    }
}
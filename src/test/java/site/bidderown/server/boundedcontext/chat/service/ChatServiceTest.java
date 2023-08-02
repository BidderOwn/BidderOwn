package site.bidderown.server.boundedcontext.chat.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.boundedcontext.chat.controller.dto.ChatMessageRequest;
import site.bidderown.server.boundedcontext.chat.controller.dto.ChatResponse;
import site.bidderown.server.boundedcontext.chat.entity.Chat;
import site.bidderown.server.boundedcontext.chat.repository.ChatRepository;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.chatroom.repository.ChatRoomRepository;
import site.bidderown.server.boundedcontext.chatroom.service.ChatRoomService;
import site.bidderown.server.boundedcontext.image.service.ImageService;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemRequest;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.repository.ItemRepository;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatServiceTest {

    @Autowired
    ChatService chatService;

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    ChatRoomService chatRoomService;

    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    ImageService imageService;

    @Autowired
    ItemRepository itemRepository;

    Member createMember(String username) {
        return memberService.join(username,"");
    }

    Item createItem(Member member) {
        Item item = itemRepository.save(
                Item.of(
                        ItemRequest.builder()
                                .title("title")
                                .description("description")
                                .period(3)
                                .minimumPrice(10000)
                                .build(), member));
        imageService.create(item, List.of("image1.jpeg"));
        return item;
    }

    @Test
    @DisplayName("채팅 생성")
    void t01() {
        //given
        Member seller = createMember("seller");
        Member buyer = createMember("buyer");
        Item item = createItem(seller);
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(seller, buyer, item));
        ChatMessageRequest chatMessageRequest = ChatMessageRequest.builder()
                .chatRoomId(chatRoom.getId())
                .message("test")
                .username(buyer.getName())
                .build();

        //when
        ChatResponse chatResponse = chatService.create(chatMessageRequest);
        Chat chat = chatRepository.findByChatRoomOrderByIdDesc(chatRoom).get(0);

        //then
        assertThat(chatResponse.getRoomId()).isEqualTo(chat.getChatRoom().getId());
        assertThat(chatResponse.getSender()).isEqualTo(chat.getSender().getName());
        assertThat(chatResponse.getMessage()).isEqualTo(chat.getMessage());
    }

    @Test
    @DisplayName("채팅 조회")
    void t02() {
        //given
        Member seller = createMember("seller");
        Member buyer = createMember("buyer");
        Item item = createItem(seller);
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.of(seller, buyer, item));
        ChatMessageRequest chatMessageRequest1 = ChatMessageRequest.builder()
                .chatRoomId(chatRoom.getId())
                .message("hi")
                .username(buyer.getName())
                .build();

        ChatMessageRequest chatMessageRequest2 = ChatMessageRequest.builder()
                .chatRoomId(chatRoom.getId())
                .message("hello")
                .username(seller.getName())
                .build();

        ChatResponse chatResponse1 = chatService.create(chatMessageRequest1);
        ChatResponse chatResponse2 = chatService.create(chatMessageRequest2);

        //when
        List<ChatResponse> chatResponses = chatService.getChats(chatRoom.getId());

        //then
        assertThat(chatResponses.get(0).getSender()).isEqualTo(seller.getName());
        assertThat(chatResponses.get(1).getSender()).isEqualTo(buyer.getName());

    }
}
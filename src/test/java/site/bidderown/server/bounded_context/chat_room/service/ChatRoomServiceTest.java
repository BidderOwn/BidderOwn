package site.bidderown.server.bounded_context.chat_room.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.ErrorCode;
import site.bidderown.server.base.exception.custom_exception.SoldOutItemException;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomRepository;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ChatRoomServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private ChatRoomRepository chatRoomRepository;

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

    @DisplayName("채팅방 생성")
    @Test
    void t1(){
        //given
        Member seller = createUser("member1");
        Member buyer = createUser("member2");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        //when
        Long chatRoomId = chatRoomService.handleChatRoom(ChatRoomRequest.of(buyer.getName(), item.getId()));

        //then
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        assertThat(chatRoom.getSeller().getName()).isEqualTo("member1");
        assertThat(chatRoom.getBuyer().getName()).isEqualTo("member2");
        assertThat(chatRoom.getItem()).isEqualTo(item);
    }

    @DisplayName("판매가 종료된 아이템에 대해 채팅을 생성할 수 없음")
    @Test
    void t2(){
        //given
        Member seller = createUser("member1");
        Member buyer = createUser("member2");
        Item item = createItem(seller, "item1", "itemDescription", 10000);
        item.updateStatus(ItemStatus.SOLDOUT);

        //when
        SoldOutItemException exception = assertThrows(
                SoldOutItemException.class,
                () -> chatRoomService.handleChatRoom(ChatRoomRequest.of(buyer.getName(), item.getId()))
        );

        //then
        assertThat(exception.getMessage()).isEqualTo("판매가 종료된 아이템입니다.");
    }

    @DisplayName("채팅 방 정보 조회 - 조회한 사람이 판매자일 때")
    @Test
    void t3(){
        /**
         * 판매자 화면에서 채팅방을 조회했을 때 ToUser는 구매자가 됩니다.
         */

        //given
        Member seller = createUser("member1");
        Member buyer = createUser("member2");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        ChatRoom chatRoom = chatRoomService.create(seller, buyer, item);

        //when
        ChatRoomDetail chatRoomDetail = chatRoomService.getChatRoomDetail(chatRoom.getId(), seller.getName());

        //then
        assertThat(chatRoomDetail.getItemId()).isEqualTo(item.getId());
        assertThat(chatRoomDetail.getPrice()).isEqualTo(item.getMinimumPrice());
        assertThat(chatRoomDetail.getItemTitle()).isEqualTo(item.getTitle());
        assertThat(chatRoomDetail.getToUserId()).isEqualTo(buyer.getId());
    }
    @DisplayName("채팅 방 정보 조회 - 조회한 사람이 구매자일 때")
    @Test
    void t4(){
        /**
         *구매자 화면에서 채팅방을 조회했을 때 ToUser는 판매자가 됩니다.
         */

        //given
        Member seller = createUser("member1");
        Member buyer = createUser("member2");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        ChatRoom chatRoom = chatRoomService.create(seller, buyer, item);

        //when
        ChatRoomDetail chatRoomDetail = chatRoomService.getChatRoomDetail(chatRoom.getId(), buyer.getName());

        //then
        assertThat(chatRoomDetail.getItemId()).isEqualTo(item.getId());
        assertThat(chatRoomDetail.getPrice()).isEqualTo(item.getMinimumPrice());
        assertThat(chatRoomDetail.getItemTitle()).isEqualTo(item.getTitle());
        assertThat(chatRoomDetail.getToUserId()).isEqualTo(seller.getId());
    }

    @DisplayName("채팅 방 전체 조회")
    @Test
    void t5(){
        //given
        Member seller = createUser("member1");
        Member buyer1 = createUser("member2");
        Member buyer2 = createUser("member3");
        Member buyer3 = createUser("member4");
        Item item = createItem(seller, "item1", "itemDescription", 10000);

        ChatRoom chatRoom1 = chatRoomService.create(seller, buyer1, item);
        ChatRoom chatRoom2 = chatRoomService.create(seller, buyer2, item);
        ChatRoom chatRoom3 = chatRoomService.create(seller, buyer3, item);

        //when
        List<ChatRoomResponse> chatRooms = chatRoomService.getChatRooms(seller.getName());

        //then
        List<String> receiverNames = chatRooms.stream()
                .map(chatRoomResponse -> chatRoomResponse.getToUserName())
                .collect(Collectors.toList());
        assertThat("member2").isIn(receiverNames);
        assertThat("member3").isIn(receiverNames);
        assertThat("member4").isIn(receiverNames);

    }
}
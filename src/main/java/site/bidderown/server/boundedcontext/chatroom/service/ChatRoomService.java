package site.bidderown.server.boundedcontext.chatroom.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.base.exception.custom_exception.SoldOutItemException;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomDetail;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomRequest;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomResponse;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.chatroom.repository.ChatRoomCustomRepository;
import site.bidderown.server.boundedcontext.chatroom.repository.ChatRoomRepository;
import site.bidderown.server.boundedcontext.chatroom.repository.dto.ChatRoomInfo;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomCustomRepository chatRoomCustomRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅방입니다.", chatRoomId + ""));
    }


    public ChatRoom create(Member seller, Member buyer, Item item) {
        return chatRoomRepository.save(ChatRoom.of(seller, buyer, item));
    }

    @Transactional
    public Long handleChatRoom(ChatRoomRequest chatRoomRequest) {
        if(chatRoomRequest.getItemId() == null)
            return -1L;

        Item item = itemService.getItem(chatRoomRequest.getItemId());
        Member buyer = memberService.getMember(chatRoomRequest.getBuyerName());
        Member seller = item.getMember();

        if (item.getItemStatus() == ItemStatus.SOLDOUT)
            throw new SoldOutItemException("판매가 종료된 아이템입니다.", item.getId() + "");

        Optional<ChatRoom> opChatRoom = chatRoomRepository
                .findChatRoomByBuyerAndItem(buyer, item);;

        return opChatRoom.orElseGet(() ->
                create(seller, buyer, item)).getId();
    }

    public ChatRoomDetail getChatRoomDetail(Long chatRoomId, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 채팅방입니다.", chatRoomId + ""));

        ChatRoomInfo chatRoomInfo = chatRoomCustomRepository.findById(chatRoomId);

        return ChatRoomDetail.of(chatRoomInfo, username);
    }

    public List<ChatRoomResponse> getChatRooms(String memberName) {
        Member member = memberService.getMember(memberName);
        return chatRoomRepository
                .findChatRoomsBySellerOrBuyer(member, member)
                .stream()
                .map(chatRoom -> ChatRoomResponse.of(
                        chatRoom,
                        memberName,
                        chatRoom.getItem().getTitle())
                )
                .collect(Collectors.toList());
    }

    public void clear() {
        chatRoomRepository.deleteAll();
    }
}

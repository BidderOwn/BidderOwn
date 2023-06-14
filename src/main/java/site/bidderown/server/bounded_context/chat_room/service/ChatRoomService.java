package site.bidderown.server.bounded_context.chat_room.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomCustomRepository;
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomRepository;
import site.bidderown.server.bounded_context.chat_room.repository.dto.ChatRoomInfo;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

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
                .orElseThrow(() -> new NotFoundException(chatRoomId));
    }

    @Transactional
    public ChatRoom create(ChatRoomRequest chatRoomRequest) {
        Member buyer = memberService.getMember(chatRoomRequest.getBuyerName());
        Item item = itemService.getItem(chatRoomRequest.getItemId());

        return chatRoomRepository.save(ChatRoom.of(item.getMember(), buyer, item));
    }

    @Transactional
    public Long handleChatRoom(ChatRoomRequest chatRoomRequest) {
        Member buyer = memberService.getMember(chatRoomRequest.getBuyerName());
        Item item = itemService.getItem(chatRoomRequest.getItemId());

        Optional<ChatRoom> opChatRoom = chatRoomRepository
                .findChatRoomByBuyerAndItem(buyer, item);

        return opChatRoom.orElseGet(() ->
                chatRoomRepository.save(ChatRoom.of(item.getMember(), buyer, item))).getId();
    }

    public ChatRoomDetail getChatRoomDetail(Long id, String username) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));

        ChatRoomInfo chatRoomInfo = chatRoomCustomRepository.findById(id);

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

    private List<ChatResponse> getChatList(Long chatRoomId) {
        /**
         * 채팅방의 모든 채팅 기록 가져오기
         * @param chatRoomId: 방 ID
         * @return List<ChatResponse>: 채팅 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */
        return getChatRoom(chatRoomId)
                .getChatList()
                .stream()
                .map(ChatResponse::of)
                .collect(Collectors.toList());
    }

    public void clear() {
        chatRoomRepository.deleteAll();
    }
}

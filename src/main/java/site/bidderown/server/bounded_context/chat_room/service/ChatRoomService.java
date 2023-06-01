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
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomRepository;
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
    private final MemberService memberService;
    private final ItemService itemService;

    public ChatRoom getChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(chatRoomId));
    }

    @Transactional
    public ChatRoom create(ChatRoomRequest chatRoomRequest) {
        Member seller = memberService.getMember(chatRoomRequest.getSellerId());
        Member buyer = memberService.getMember(chatRoomRequest.getBuyerId());
        Item item = itemService.getItem(chatRoomRequest.getItemId());

        return chatRoomRepository.save(ChatRoom.of(seller, buyer, item));
    }

    @Transactional
    public Long handleChatRoom(ChatRoomRequest chatRoomRequest) {
        Member seller = memberService.getMember(chatRoomRequest.getSellerId());
        Member buyer = memberService.getMember(chatRoomRequest.getBuyerId());
        Item item = itemService.getItem(chatRoomRequest.getItemId());

        Optional<ChatRoom> chatRoom = chatRoomRepository
                .findChatRoomBySellerAndBuyerAndItem(seller, buyer, item);

        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        return chatRoomRepository.save(ChatRoom.of(seller, buyer, item)).getId();
    }

    public ChatRoomDetail getChatRoomDetail(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        List<ChatResponse> chatList = getChatList(id);
        // TODO 상품의 첫 이미지 이름 가져오기 chatRoom.getItem().getImages().get(0)
        return ChatRoomDetail.of(chatRoom.getItem(), chatList);
    }

    public List<ChatRoomResponse> getChatRooms(Long memberId) {
        /**
         * 내가 속한 모든 채팅방 가져오기 (본인이 구매자, 판매자일 경우)
         * @param memberId: 내 아이디
         * @return List<ChatRoomResponse>: 채팅방 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */

        Member member = memberService.getMember(memberId);
        return chatRoomRepository
                // 본인이 구매자와 판매자일 경우의 모든 채팅방을 찾음
                .findChatRoomsBySellerOrBuyer(member, member)
                .stream()
                .map(chatRoom -> ChatRoomResponse.of(chatRoom, member.getName()))
                .collect(Collectors.toList());
    }

    public List<ChatRoomResponse> getChatRooms(String memberName) {
        /**
         * 내가 속한 모든 채팅방 가져오기 (본인이 구매자, 판매자일 경우)
         * @param memberId: 내 아이디
         * @return List<ChatRoomResponse>: 채팅방 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */

        Member member = memberService.getMember(memberName);
        return chatRoomRepository
                // 본인이 구매자와 판매자일 경우의 모든 채팅방을 찾음
                .findChatRoomsBySellerOrBuyer(member, member)
                .stream()
                .map(chatRoom -> ChatRoomResponse.of(chatRoom, member.getName()))
                .collect(Collectors.toList());
    }
//findChatListByChatRoomId
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

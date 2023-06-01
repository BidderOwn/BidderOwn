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
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
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
    private final ItemRepository itemRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    @Transactional
    public ChatRoom create(ChatRoomRequest chatRoomRequest) {
        Member seller = memberService.findById(chatRoomRequest.getSellerId());
        Member buyer = memberService.findById(chatRoomRequest.getBuyerId());
        Item item = itemRepository.findById(chatRoomRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(chatRoomRequest.getItemId()));

        return chatRoomRepository.save(ChatRoom.of(seller, buyer, item));
    }

    @Transactional
    public Long handleChatRoom(ChatRoomRequest chatRoomRequest) {
        Member seller = memberService.findById(chatRoomRequest.getSellerId());
        Member buyer = memberService.findById(chatRoomRequest.getBuyerId());
        Item item = itemRepository.findById(chatRoomRequest.getItemId())
                .orElseThrow(() -> new NotFoundException(chatRoomRequest.getItemId()));

        Optional<ChatRoom> chatRoom = chatRoomRepository
                .findChatRoomBySellerAndBuyerAndItem(seller, buyer, item);

        if (chatRoom.isPresent()) {
            return chatRoom.get().getId();
        }

        return chatRoomRepository.save(ChatRoom.of(seller, buyer, item)).getId();
    }

    public ChatRoomDetail findChatRoomDetailById(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
        List<ChatResponse> chatList = findChatListByChatRoomId(id);
        // TODO 상품의 첫 이미지 이름 가져오기 chatRoom.getItem().getImages().get(0)
        return ChatRoomDetail.of(chatRoom.getItem(), chatList);
    }

    public List<ChatRoomResponse> findAllByMemberId(Long memberId) {
        /**
         * 내가 속한 모든 채팅방 가져오기 (본인이 구매자, 판매자일 경우)
         * @param memberId: 내 아이디
         * @return List<ChatRoomResponse>: 채팅방 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */

        Member member = memberService.findById(memberId);
        return chatRoomRepository
                // 본인이 구매자와 판매자일 경우의 모든 채팅방을 찾음
                .findChatRoomsBySellerOrBuyer(member, member)
                .stream()
                .map(chatRoom -> ChatRoomResponse.of(chatRoom, member.getName()))
                .collect(Collectors.toList());
    }

    public List<ChatRoomResponse> findAllByMemberName(String memberName) {
        /**
         * 내가 속한 모든 채팅방 가져오기 (본인이 구매자, 판매자일 경우)
         * @param memberId: 내 아이디
         * @return List<ChatRoomResponse>: 채팅방 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */

        Member member = memberService.findByName(memberName);
        return chatRoomRepository
                // 본인이 구매자와 판매자일 경우의 모든 채팅방을 찾음
                .findChatRoomsBySellerOrBuyer(member, member)
                .stream()
                .map(chatRoom -> ChatRoomResponse.of(chatRoom, member.getName()))
                .collect(Collectors.toList());
    }

    private List<ChatResponse> findChatListByChatRoomId(Long chatRoomId) {
        /**
         * 채팅방의 모든 채팅 기록 가져오기
         * @param chatRoomId: 방 ID
         * @return List<ChatResponse>: 채팅 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */
        return findById(chatRoomId)
                .getChatList()
                .stream()
                .map(ChatResponse::of)
                .collect(Collectors.toList());
    }

    public ChatRoom findById(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new NotFoundException(chatRoomId));
    }

    public void clear() {
        chatRoomRepository.deleteAll();
    }
}

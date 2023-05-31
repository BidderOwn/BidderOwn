package site.bidderown.server.bounded_context.chat_room.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.repository.ChatRoomRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberService memberService;

    @Transactional
    public ChatRoom create(ChatRoomRequest chatRoomRequest) {
        Member seller = memberService.findById(chatRoomRequest.getSellerId());
        Member buyer = memberService.findById(chatRoomRequest.getBuyerId());
        return chatRoomRepository.save(ChatRoom.of(seller, buyer));
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
                .map(chatRoom -> ChatRoomResponse.from(chatRoom, memberId))
                .collect(Collectors.toList());
    }

    public List<ChatResponse> findChatListByChatRoomId(Long chatRoomId) {
        /**
         * 채팅방의 모든 채팅 기록 가져오기
         * @param chatRoomId: 방 ID
         * @return List<ChatResponse>: 채팅 목록
         * TODO Paging 처리, QueryDsl 적용 여부
         */
        return findById(chatRoomId)
                .getChatList()
                .stream()
                .map(ChatResponse::from)
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

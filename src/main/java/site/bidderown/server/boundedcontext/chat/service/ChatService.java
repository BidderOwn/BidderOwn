package site.bidderown.server.boundedcontext.chat.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.boundedcontext.chat.controller.dto.ChatMessageRequest;
import site.bidderown.server.boundedcontext.chat.controller.dto.ChatResponse;
import site.bidderown.server.boundedcontext.chat.entity.Chat;
import site.bidderown.server.boundedcontext.chat.repository.ChatRepository;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.chatroom.service.ChatRoomService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatService {

    private final ChatRepository chatRepository;
    private final ChatRoomService chatRoomService;
    private final MemberService memberService;

    public ChatResponse create(ChatMessageRequest chatMessageRequest) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatMessageRequest.getChatRoomId());
        Member member = memberService.getMember(chatMessageRequest.getUsername());

        Chat chat = chatRepository.save(Chat.of(chatMessageRequest.getMessage(), member, chatRoom));
        return ChatResponse.of(chat);
    }

    public List<ChatResponse> getChats(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatRoomId);
        return chatRepository.findByChatRoomOrderByIdDesc(chatRoom)
                .stream()
                .map(ChatResponse::of)
                .collect(Collectors.toList());
    }
}

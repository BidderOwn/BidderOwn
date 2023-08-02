package site.bidderown.server.boundedcontext.chatroom.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomDetail;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomRequest;
import site.bidderown.server.boundedcontext.chatroom.controller.dto.ChatRoomResponse;
import site.bidderown.server.boundedcontext.chatroom.service.ChatRoomService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "채팅룸 chat-room-api-controller", description = "채팅룸 관련 api 입니다.")
@RequestMapping("/api/v1/chat-room")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 목록", description = "참여중인 채팅방 목록을 가져옵니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public List<ChatRoomResponse> findChatRoomList(@AuthenticationPrincipal User user) {
        return chatRoomService.getChatRooms(user.getUsername());
    }

    @Operation(summary = "채팅방 상세정보", description = "채팅방 id를 이용해 채팅방에 대한 상세 정보를 조회합니다.")
    @GetMapping("/detail/{chatRoomId}")
    public ChatRoomDetail getChatRoomDetail(@AuthenticationPrincipal User user, @PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomDetail(chatRoomId, user.getUsername());
    }

    @Operation(summary = "채팅방 생성", description = "itemId와 buyerName을 이용해 기존 채팅룸이 있다면 chatRoomId를 반환하고 없다면 생성하여 반환합니다.")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Long handleChatRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        return chatRoomService.handleChatRoom(chatRoomRequest);
    }
}

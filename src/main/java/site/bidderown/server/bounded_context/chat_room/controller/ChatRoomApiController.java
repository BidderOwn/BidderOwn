package site.bidderown.server.bounded_context.chat_room.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;

import java.util.List;

@Tag(name = "chat-room", description = "채팅방 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-room")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @Operation(summary = "채팅방 리스트 조회", description = "username을 이용하여 모든 chatRoom 리스트를 조회합니다.")
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public List<ChatRoomResponse> findChatRoomList(@AuthenticationPrincipal User user) {
        return chatRoomService.getChatRooms(user.getUsername());
    }

    @Operation(summary = "채팅방 조회", description = "chatRoomId와 username을 이용하여 chatRoom을 조회합니다.")
    @GetMapping("/detail/{chatRoomId}")
    public ChatRoomDetail getChatRoomDetail(@AuthenticationPrincipal User user, @PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomDetail(chatRoomId, user.getUsername());
    }

    @Operation(summary = "채팅방 생성", description = "chatRoomRequest를 이용하여 chatRoomId를 조회합니다.")
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Long handleChatRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        return chatRoomService.handleChatRoom(chatRoomRequest);
    }
}

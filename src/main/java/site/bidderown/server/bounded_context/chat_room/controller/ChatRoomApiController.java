package site.bidderown.server.bounded_context.chat_room.controller;

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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/chat-room")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/list")
    public List<ChatRoomResponse> findChatRoomList(@AuthenticationPrincipal User user) {
        return chatRoomService.getChatRooms(user.getUsername());
    }

    @GetMapping("/detail/{chatRoomId}")
    public ChatRoomDetail getChatRoomDetail(@AuthenticationPrincipal User user, @PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomDetail(chatRoomId, user.getUsername());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Long handleChatRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        return chatRoomService.handleChatRoom(chatRoomRequest);
    }
}

package site.bidderown.server.bounded_context.chat_room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/chat-room/list")
    public List<ChatRoomResponse> findChatRoomList(@AuthenticationPrincipal User user) {
        return chatRoomService.getChatRooms(user.getUsername());
    }

    @GetMapping("/chat-detail/{chatRoomId}")
    public ChatRoomDetail joinChat(@AuthenticationPrincipal User user, @PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomDetail(chatRoomId, user.getUsername());
    }

    @PostMapping("/chat-room")
    @PreAuthorize("isAuthenticated()")
    public String handleChatRoom(@RequestBody ChatRoomRequest chatRoomRequest){
        return "/chat/list?id=" + chatRoomService.handleChatRoom(chatRoomRequest);
    }
}

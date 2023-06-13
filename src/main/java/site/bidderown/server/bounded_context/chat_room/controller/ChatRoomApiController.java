package site.bidderown.server.bounded_context.chat_room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ChatRoomApiController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat/list")
    public List<ChatRoomResponse> findChatRoomList(@AuthenticationPrincipal User user) {
        return chatRoomService.getChatRooms(user.getUsername());
    }

    @GetMapping("/chat/{chatRoomId}")
    @ResponseBody
    public ChatRoomDetail joinChat(@PathVariable Long chatRoomId){
        return chatRoomService.getChatRoomDetail(chatRoomId);
    }

//    @GetMapping("/chat-room/ids")
//    public List<Long> getChatRoomIds(@AuthenticationPrincipal User user) {
//        chatRoomService.getChatRooms(user.getUsername());
//    }
}

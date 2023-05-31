package site.bidderown.server.bounded_context.chat_room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomResponse;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;

@RequiredArgsConstructor
@RequestMapping("/chat-room")
@Controller
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping
    @ResponseBody
    public ChatRoomResponse createChatRoom(
            @RequestBody ChatRoomRequest chatRoomRequest,
            @AuthenticationPrincipal User user
    ) {
        return ChatRoomResponse.from(chatRoomService.create(chatRoomRequest), user.getUsername());
    }

}

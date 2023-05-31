package site.bidderown.server.bounded_context.chat_room.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomDetail;
import site.bidderown.server.bounded_context.chat_room.controller.dto.ChatRoomRequest;
import site.bidderown.server.bounded_context.chat_room.service.ChatRoomService;

@RequiredArgsConstructor
@RequestMapping("/chat-room")
@Controller
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping
    public String handleChatRooms (
            Model model,
            @RequestBody ChatRoomRequest chatRoomRequest,
            @RequestParam Boolean direct
    ) {
        if (direct) {
            Long chatRoomId = chatRoomService.handleChatRoom(chatRoomRequest);
            model.addAttribute("chatRoomId", chatRoomId);
        }
        return "/usr/chat/list";
    }

    @GetMapping("/{chatRoomId}")
    @ResponseBody
    public ChatRoomDetail joinChat(@PathVariable Long chatRoomId){
        return chatRoomService.findChatRoomDetailById(chatRoomId);
    }
}

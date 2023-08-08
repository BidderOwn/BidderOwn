package site.bidderown.server.boundedcontext.chatroom.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.boundedcontext.chatroom.service.ChatRoomService;

@RequiredArgsConstructor
@Controller
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @GetMapping("/chat-room/list")
    @PreAuthorize("isAuthenticated()")
    public String showChatRoomList (
            Model model,
            @RequestParam(value = "itemId", required = false) Long itemId,
            @RequestParam(value = "buyerName", required = false) String buyerName
    ) {
        /**
         *  채팅방 리스트 페이지로 이동
         *  model에 담겨있는 채팅방에 자동
         */
        model.addAttribute("itemId", itemId);
        model.addAttribute("buyerName", buyerName);

        return "usr/chatroom/list";
    }
}

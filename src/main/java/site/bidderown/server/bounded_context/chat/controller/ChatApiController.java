package site.bidderown.server.bounded_context.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat.service.ChatService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatApiController {
    private final ChatService chatService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/chat/list/{chatRoomId}")
    public List<ChatResponse> chatHistory(@PathVariable Long chatRoomId) {
        return chatService.getChats(chatRoomId);
    }

}

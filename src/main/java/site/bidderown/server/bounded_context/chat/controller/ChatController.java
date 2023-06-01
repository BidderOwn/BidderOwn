package site.bidderown.server.bounded_context.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatRequest;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat.service.ChatService;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatRequest chatRequest, @AuthenticationPrincipal User user) {

        ChatResponse chatResponse = chatService.create(chatRequest, user.getUsername());

        messagingTemplate.convertAndSend("/sub/chat/room/" + chatResponse.getRoomId(), chatResponse);
    }
}

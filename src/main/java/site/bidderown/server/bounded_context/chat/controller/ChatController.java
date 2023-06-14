package site.bidderown.server.bounded_context.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatMessageRequest;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatNotificationRequest;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat.service.ChatService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatController {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(ChatMessageRequest chatMessageRequest) {
        ChatResponse chatResponse = chatService.create(chatMessageRequest);
        messagingTemplate.convertAndSend("/sub/chat-room/" + chatResponse.getRoomId(), chatResponse);
    }

    @MessageMapping("/chat/notification")
    public void chatNotification(ChatNotificationRequest chatNotificationRequest) {
        messagingTemplate.convertAndSend("/sub/chat/user/" + chatNotificationRequest.getToUserId(),
                chatNotificationRequest.getToUsername());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/v1/chat/list/{chatRoomId}")
    public List<ChatResponse> chatHistory(@PathVariable Long chatRoomId) {
        return chatService.getChats(chatRoomId);
    }
}

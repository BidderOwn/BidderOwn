package site.bidderown.server.bounded_context.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatMessageRequest;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatNotificationRequest;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat.service.ChatService;


@RequiredArgsConstructor
@Controller
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

}

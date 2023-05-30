package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.Builder;
import lombok.Setter;
import site.bidderown.server.bounded_context.chat.entity.Chat;

import java.time.LocalDateTime;

@Setter
@Builder
public class ChatResponse {
    private Long senderId;
    private String message;
    private LocalDateTime createAt;

    public static ChatResponse from(Chat chat) {
        return ChatResponse
                .builder()
                .senderId(chat.getSender().getId())
                .message(chat.getMessage())
                .createAt(chat.getCreatedAt())
                .build();
    }
}

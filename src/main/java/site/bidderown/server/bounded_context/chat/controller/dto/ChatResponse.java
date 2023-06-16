package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.bounded_context.chat.entity.Chat;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatResponse {
    private Long roomId;
    private String message;
    private String sender;
    private LocalDateTime createAt;

    @Builder
    private ChatResponse(String sender, Long roomId, String message, LocalDateTime createAt) {
        this.sender = sender;
        this.roomId = roomId;
        this.message = message;
        this.createAt = createAt;
    }
    public static ChatResponse of(Chat chat) {
        return ChatResponse
                .builder()
                .sender(chat.getSender().getName())
                .roomId(chat.getChatRoom().getId())
                .message(chat.getMessage())
                .createAt(chat.getCreatedAt())
                .build();
    }

}

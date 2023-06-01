package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.bounded_context.chat.entity.Chat;

import java.time.LocalDateTime;

@Setter
@Getter
public class ChatResponse {
    private Long senderId;
    private Long roomId;
    private String message;
    private LocalDateTime createAt;

    @Builder
    private ChatResponse(Long senderId, Long roomId,String message, LocalDateTime createAt) {
        this.senderId = senderId;
        this.roomId = roomId;
        this.message = message;
        this.createAt = createAt;
    }
    public static ChatResponse of(Chat chat) {
        return ChatResponse
                .builder()
                .senderId(chat.getSender().getId())
                .roomId(chat.getChatRoom().getId())
                .message(chat.getMessage())
                .createAt(chat.getCreatedAt())
                .build();
    }

}

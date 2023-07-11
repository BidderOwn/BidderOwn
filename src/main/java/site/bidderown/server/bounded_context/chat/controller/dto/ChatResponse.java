package site.bidderown.server.bounded_context.chat.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.bounded_context.chat.entity.Chat;

import java.time.LocalDateTime;

@Schema(description = "채팅 응답")
@Setter
@Getter
public class ChatResponse {
    @Schema(description = "채팅방 ID")
    private Long roomId;
    @Schema(description = "메세지 내용")
    private String message;
    @Schema(description = "보내는 사람 이름")
    private String sender;
    @Schema(description = "생성 일자")
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

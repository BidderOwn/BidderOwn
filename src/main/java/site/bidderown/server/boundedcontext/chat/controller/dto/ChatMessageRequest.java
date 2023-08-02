package site.bidderown.server.boundedcontext.chat.controller.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRequest {
    private Long chatRoomId;
    private String message;
    private String username;

    @Builder
    public ChatMessageRequest(Long chatRoomId, String message, String username) {
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.username = username;
    }
}


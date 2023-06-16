package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRequest {
    private Long chatRoomId;
    private String message;
    private String username;
}


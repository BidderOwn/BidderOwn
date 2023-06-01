package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRequest {
    private Long roomId;
    private String message;
    private LocalDateTime createAt;
    private String sender;
}

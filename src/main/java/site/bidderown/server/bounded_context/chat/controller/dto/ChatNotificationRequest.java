package site.bidderown.server.bounded_context.chat.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatNotificationRequest {
    private Long toUserId;
    private String toUsername;
}

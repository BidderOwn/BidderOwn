package site.bidderown.server.boundedcontext.chat.controller.dto;

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

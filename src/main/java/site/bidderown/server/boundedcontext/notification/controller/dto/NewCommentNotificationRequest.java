package site.bidderown.server.boundedcontext.notification.controller.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewCommentNotificationRequest {
    private Long itemId;
    private String writerName;
}

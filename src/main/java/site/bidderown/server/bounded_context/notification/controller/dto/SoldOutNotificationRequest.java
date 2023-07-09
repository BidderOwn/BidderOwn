package site.bidderown.server.bounded_context.notification.controller.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SoldOutNotificationRequest {
    private Long itemId;
}

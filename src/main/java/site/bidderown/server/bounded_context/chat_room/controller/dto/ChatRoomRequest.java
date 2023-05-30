package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatRoomRequest {
    private Long sellerId;
    private Long buyerId;

}

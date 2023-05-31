package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatRoomRequest {
    private Long sellerId;
    private Long buyerId;

    @Builder
    public ChatRoomRequest(Long sellerId, Long buyerId) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
    }

    public static ChatRoomRequest from(Long sellerId, Long buyerId) {
        return ChatRoomRequest
                .builder()
                .sellerId(sellerId)
                .buyerId(buyerId)
                .build();
    }
}

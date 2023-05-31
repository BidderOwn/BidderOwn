package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatRoomRequest {
    private Long sellerId;
    private Long buyerId;
    private Long itemId;

    @Builder
    public ChatRoomRequest(Long sellerId, Long buyerId, Long itemId) {
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemId = itemId;
    }

    public static ChatRoomRequest from(Long sellerId, Long buyerId, Long itemId) {
        return ChatRoomRequest
                .builder()
                .sellerId(sellerId)
                .buyerId(buyerId)
                .itemId(itemId)
                .build();
    }
}

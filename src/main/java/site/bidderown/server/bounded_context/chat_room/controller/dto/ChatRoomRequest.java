package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequest {
    private String buyerName;
    private Long itemId;

    @Builder
    public ChatRoomRequest(String buyerName, Long itemId) {
        this.buyerName = buyerName;
        this.itemId = itemId;
    }

    public static ChatRoomRequest of(String buyerName, Long itemId) {
        return ChatRoomRequest
                .builder()
                .buyerName(buyerName)
                .itemId(itemId)
                .build();
    }
}

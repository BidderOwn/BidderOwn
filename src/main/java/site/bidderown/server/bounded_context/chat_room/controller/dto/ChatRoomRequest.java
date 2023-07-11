package site.bidderown.server.bounded_context.chat_room.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "채팅방 요청")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomRequest {
    @Schema(description = "구매자이름")
    private String buyerName;
    @Schema(description = "상품ID")
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

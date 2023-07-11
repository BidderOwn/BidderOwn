package site.bidderown.server.bounded_context.chat_room.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.chat_room.repository.dto.ChatRoomInfo;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.util.List;

@Schema(description = "채팅방 상세")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetail {
    @Schema(description = "상대유저 ID")
    private Long toUserId;
    @Schema(description = "상품 제목")
    private String itemTitle;
    @Schema(description = "상품 썸네일사진 이름")
    private String itemImageName;
    @Schema(description = "상품가격")
    private int price;
    @Schema(description = "상품ID")
    private Long itemId;

    @Builder
    private ChatRoomDetail(Long toUserId, String itemTitle, String itemImageName, int price, Long itemId){
        this.toUserId = toUserId;
        this.itemTitle = itemTitle;
        this.itemImageName = itemImageName;
        this.price = price;
        this.itemId = itemId;
    }

    public static ChatRoomDetail of(ChatRoomInfo chatRoomInfo, String fromUsername) {
        return ChatRoomDetail.builder()
                .toUserId(ChatRoom.resolveToMember(chatRoomInfo.getChatRoom(), fromUsername).getId())
                .itemTitle(chatRoomInfo.getItem().getTitle())
                .itemImageName(chatRoomInfo.getItem().getImages().get(0).getFileName())
                .price(chatRoomInfo.getItem().getMinimumPrice())
                .itemId(chatRoomInfo.getItem().getId())
                .build();
    }
}

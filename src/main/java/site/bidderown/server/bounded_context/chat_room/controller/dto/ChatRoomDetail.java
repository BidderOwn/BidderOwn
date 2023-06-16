package site.bidderown.server.bounded_context.chat_room.controller.dto;

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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetail {
    private Long toUserId;
    private String itemTitle;
    private String itemImageName;
    private int price;
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

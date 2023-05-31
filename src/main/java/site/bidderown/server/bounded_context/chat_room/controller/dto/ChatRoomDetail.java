package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.chat.controller.dto.ChatResponse;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomDetail {
    private String itemTitle;
    private String itemImageName;
    private int price;
    private List<ChatResponse> chatList; // 시간이랑 내용은 여기에서 끌어 쓰는게 좋겠죠??

    @Builder
    private ChatRoomDetail(String itemTitle, String itemImageName, int price, List<ChatResponse> chatList){
        this.itemTitle = itemTitle;
        this.itemImageName = itemImageName;
        this.price = price;
        this.chatList = chatList;
    }

    public static ChatRoomDetail of(Item item, List<ChatResponse> chatList){
        return ChatRoomDetail.builder()
                .itemTitle(item.getTitle())
                .itemImageName("")
                .price(item.getMinimumPrice())
                .chatList(chatList)
                .build();
    }
}

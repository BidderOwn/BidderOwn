package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {
    private Long chatRoomId;
    private String toUserName;
    private String itemTitle;

    @Builder
    public ChatRoomResponse(Long chatRoomId, String toUserName, String itemTitle){
        this.chatRoomId = chatRoomId;
        this.toUserName = toUserName;
        this.itemTitle = itemTitle;
    }

    public static ChatRoomResponse of(ChatRoom chatRoom, String fromName, String itemTitle) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .toUserName(ChatRoom.resolveToMember(chatRoom, fromName).getName())
                .itemTitle(itemTitle)
                .build();
    }
}

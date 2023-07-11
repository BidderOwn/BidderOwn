package site.bidderown.server.bounded_context.chat_room.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;

@Schema(description = "채팅방 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {
    @Schema(description = "채팅방ID")
    private Long chatRoomId;
    @Schema(description = "상대유저 이름")
    private String toUserName;

    @Builder
    public ChatRoomResponse(Long chatRoomId, String toUserName){
        this.chatRoomId = chatRoomId;
        this.toUserName = toUserName;

    }

    public static ChatRoomResponse of(ChatRoom chatRoom, String fromName, String itemTitle) {
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .toUserName(ChatRoom.resolveToMember(chatRoom, fromName).getName())
                .build();
    }
}

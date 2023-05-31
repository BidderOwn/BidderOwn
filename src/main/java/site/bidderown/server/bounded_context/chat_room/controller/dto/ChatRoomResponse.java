package site.bidderown.server.bounded_context.chat_room.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {
    private Long chatRoomId;
    private String toName;
    private String toProfileImageName;

    @Builder
    public ChatRoomResponse(Long chatRoomId, String toName, String toProfileImageName){
        this.chatRoomId = chatRoomId;
        this.toName= toName;
        this.toProfileImageName = toProfileImageName;
    }

    public static ChatRoomResponse from(ChatRoom chatRoom, Long fromId) {
        //TODO 프로필 이미지 추가
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .toName(getToName(chatRoom, fromId))
                .build();
    }
    
    public static String getToName(ChatRoom chatRoom, Long fromId) {
        return chatRoom.getBuyer().getId().equals(fromId) ?
                chatRoom.getSeller().getName():
                chatRoom.getBuyer().getName();
    }
}

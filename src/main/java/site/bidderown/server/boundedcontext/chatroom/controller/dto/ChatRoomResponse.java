package site.bidderown.server.boundedcontext.chatroom.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.member.entity.Member;

@Schema(description = "채팅방 응답")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoomResponse {
    @Schema(description = "채팅방ID")
    private Long chatRoomId;
    @Schema(description = "상대유저 ID")
    private Long toUserId;
    @Schema(description = "상대유저 이름")
    private String toUserName;
    @Schema(description = "채팅방 상품 이름")
    private String itemTitle;

    @Builder
    public ChatRoomResponse(Long chatRoomId, Long toUserId, String toUserName, String itemTitle){
        this.chatRoomId = chatRoomId;
        this.toUserId = toUserId;
        this.toUserName = toUserName;
        this.itemTitle = itemTitle;
    }

    public static ChatRoomResponse of(ChatRoom chatRoom, String fromName, String itemTitle) {
        Member toMember =  ChatRoom.resolveToMember(chatRoom, fromName);
        return ChatRoomResponse.builder()
                .chatRoomId(chatRoom.getId())
                .toUserId(toMember.getId())
                .toUserName(toMember.getName())
                .itemTitle(itemTitle)
                .build();
    }
}

package site.bidderown.server.boundedcontext.chatroom.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomInfo {
    private Member seller;
    private Member buyer;
    private ChatRoom chatRoom;
    private Item item;
}

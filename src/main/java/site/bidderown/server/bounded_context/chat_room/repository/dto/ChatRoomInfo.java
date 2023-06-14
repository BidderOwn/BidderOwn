package site.bidderown.server.bounded_context.chat_room.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter
@Setter
@AllArgsConstructor
public class ChatRoomInfo {
    private Member seller;
    private Member buyer;
    private ChatRoom chatRoom;
    private Item item;
}

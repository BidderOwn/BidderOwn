package site.bidderown.server.boundedcontext.chatroom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomsBySellerOrBuyer(Member seller, Member buyer);
    Optional<ChatRoom> findChatRoomByBuyerAndItem(Member buyer, Item item);
}

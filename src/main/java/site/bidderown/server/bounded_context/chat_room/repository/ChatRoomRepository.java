package site.bidderown.server.bounded_context.chat_room.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.member.entity.Member;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    List<ChatRoom> findChatRoomsBySellerOrBuyer(Member seller, Member buyer);
}

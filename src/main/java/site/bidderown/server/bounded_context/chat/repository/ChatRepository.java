package site.bidderown.server.bounded_context.chat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.bidderown.server.bounded_context.chat.entity.Chat;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoomOrderByIdDesc(ChatRoom chatRoom);
}

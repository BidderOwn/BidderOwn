package site.bidderown.server.boundedcontext.chat.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import site.bidderown.server.boundedcontext.chat.entity.Chat;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByChatRoomOrderByIdDesc(ChatRoom chatRoom);
}

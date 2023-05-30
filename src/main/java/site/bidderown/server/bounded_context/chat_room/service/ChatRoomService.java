package site.bidderown.server.bounded_context.chat_room.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;

@Service
@Transactional(readOnly = true)
public class ChatRoomService {
}

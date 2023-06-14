package site.bidderown.server.bounded_context.chat_room.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;



}

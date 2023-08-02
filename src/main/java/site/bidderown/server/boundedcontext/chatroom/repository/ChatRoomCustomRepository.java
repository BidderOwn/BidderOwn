package site.bidderown.server.boundedcontext.chatroom.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.chatroom.repository.dto.ChatRoomInfo;

import java.util.Optional;

import static site.bidderown.server.boundedcontext.chatroom.entity.QChatRoom.chatRoom;
import static site.bidderown.server.boundedcontext.item.entity.QItem.item;
import static site.bidderown.server.boundedcontext.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class ChatRoomCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public Optional<ChatRoom> findBySellerAndBuyerAndItem(Long sellerId, Long buyerId, Long itemId) {
        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(chatRoom)
                .where(
                        eqToSellerId(sellerId),
                        eqToBuyerId(buyerId),
                        eqToItemId(itemId)
                ).fetchOne());

    }

    public ChatRoomInfo findById(Long chatRoomId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        ChatRoomInfo.class,
                        chatRoom.seller,
                        chatRoom.buyer,
                        chatRoom,
                        chatRoom.item))
                .from(chatRoom)
                .where(chatRoom.id.eq(chatRoomId))
                .join(chatRoom.item, item)
                .join(chatRoom.seller, member)
                .join(chatRoom.buyer, member)
                .fetchOne();
    }

    private BooleanExpression eqToSellerId(Long sellerId) {
        return chatRoom.seller.id.eq(sellerId);
    }

    private BooleanExpression eqToBuyerId(Long buyerId) {
        return chatRoom.buyer.id.eq(buyerId);
    }

    private BooleanExpression eqToItemId(Long itemId) {
        return chatRoom.item.id.eq(itemId);
    }
}

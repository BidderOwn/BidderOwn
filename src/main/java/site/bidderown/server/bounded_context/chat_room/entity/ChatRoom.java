package site.bidderown.server.bounded_context.chat_room.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.chat.entity.Chat;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member buyer;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    @Builder
    private ChatRoom(Member seller, Member buyer, Item item) {
        this.seller = seller;
        this.buyer = buyer;
        this.item = item;
    }

    public static ChatRoom of(Member seller, Member buyer, Item item) {
        return ChatRoom.builder()
                .seller(seller)
                .buyer(buyer)
                .item(item)
                .build();
    }

    public static Member resolveToMember(ChatRoom chatRoom, String fromUsername) {
        return chatRoom.seller.getName().equals(fromUsername) ?
                chatRoom.buyer :
                chatRoom.seller;
    }

    public static Member resolveToMember(ChatRoom chatRoom, Long fromId) {
        return chatRoom.seller.getId().equals(fromId) ?
                chatRoom.buyer :
                chatRoom.seller;
    }
}

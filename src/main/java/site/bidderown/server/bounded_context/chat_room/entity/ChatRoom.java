package site.bidderown.server.bounded_context.chat_room.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
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
@Builder
@Entity
public class ChatRoom extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member buyer;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.REMOVE)
    private List<Chat> chatList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    public static ChatRoom of(Member seller, Member buyer, Item item) {
        return ChatRoom.builder()
                .item(item)
                .seller(seller)
                .buyer(buyer)
                .build();
    }

    public static Member resolveToMember(ChatRoom chatRoom, String fromUsername) {
        return chatRoom.seller.getName().equals(fromUsername) ?
                chatRoom.buyer :
                chatRoom.seller;
    }
}

package site.bidderown.server.boundedcontext.chatroom.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.chat.entity.Chat;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

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

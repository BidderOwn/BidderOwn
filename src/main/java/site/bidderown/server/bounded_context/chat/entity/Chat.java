package site.bidderown.server.bounded_context.chat.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Chat extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private ChatRoom chatRoom;

    @Column(length = 500)
    private String message;

    public static Chat of(String message, Member sender, ChatRoom chatRoom){
        return Chat.builder()
                .message(message)
                .sender(sender)
                .chatRoom(chatRoom)
                .build();
    }
}

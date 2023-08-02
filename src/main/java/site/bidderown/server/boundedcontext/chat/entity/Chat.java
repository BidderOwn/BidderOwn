package site.bidderown.server.boundedcontext.chat.entity;

import lombok.*;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.member.entity.Member;

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

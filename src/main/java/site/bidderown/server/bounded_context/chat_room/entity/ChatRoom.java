package site.bidderown.server.bounded_context.chat_room.entity;

import lombok.Builder;
import lombok.Getter;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.chat.entity.Chat;
import site.bidderown.server.bounded_context.users.entity.Users;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class ChatRoom extends BaseEntity {
    // TODO private Items item;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Users seller;

    @ManyToOne(cascade = CascadeType.REMOVE)
    private Users buyer;

    @OneToMany(mappedBy = "chatRoom")
    private List<Chat> chatList = new ArrayList<>();
}

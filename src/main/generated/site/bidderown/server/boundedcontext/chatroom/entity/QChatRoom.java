package site.bidderown.server.boundedcontext.chatroom.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -1292128809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final site.bidderown.server.base.baseentity.QBaseEntity _super = new site.bidderown.server.base.baseentity.QBaseEntity(this);

    public final site.bidderown.server.boundedcontext.member.entity.QMember buyer;

    public final ListPath<site.bidderown.server.boundedcontext.chat.entity.Chat, site.bidderown.server.boundedcontext.chat.entity.QChat> chatList = this.<site.bidderown.server.boundedcontext.chat.entity.Chat, site.bidderown.server.boundedcontext.chat.entity.QChat>createList("chatList", site.bidderown.server.boundedcontext.chat.entity.Chat.class, site.bidderown.server.boundedcontext.chat.entity.QChat.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final site.bidderown.server.boundedcontext.item.entity.QItem item;

    public final site.bidderown.server.boundedcontext.member.entity.QMember seller;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new site.bidderown.server.boundedcontext.member.entity.QMember(forProperty("buyer")) : null;
        this.item = inits.isInitialized("item") ? new site.bidderown.server.boundedcontext.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
        this.seller = inits.isInitialized("seller") ? new site.bidderown.server.boundedcontext.member.entity.QMember(forProperty("seller")) : null;
    }

}


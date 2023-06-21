package site.bidderown.server.bounded_context.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItem is a Querydsl query type for Item
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItem extends EntityPathBase<Item> {

    private static final long serialVersionUID = 1328709452L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final site.bidderown.server.base.base_entity.QBaseEntity _super = new site.bidderown.server.base.base_entity.QBaseEntity(this);

    public final ListPath<site.bidderown.server.bounded_context.bid.entity.Bid, site.bidderown.server.bounded_context.bid.entity.QBid> bids = this.<site.bidderown.server.bounded_context.bid.entity.Bid, site.bidderown.server.bounded_context.bid.entity.QBid>createList("bids", site.bidderown.server.bounded_context.bid.entity.Bid.class, site.bidderown.server.bounded_context.bid.entity.QBid.class, PathInits.DIRECT2);

    public final ListPath<site.bidderown.server.bounded_context.chat_room.entity.ChatRoom, site.bidderown.server.bounded_context.chat_room.entity.QChatRoom> chatRooms = this.<site.bidderown.server.bounded_context.chat_room.entity.ChatRoom, site.bidderown.server.bounded_context.chat_room.entity.QChatRoom>createList("chatRooms", site.bidderown.server.bounded_context.chat_room.entity.ChatRoom.class, site.bidderown.server.bounded_context.chat_room.entity.QChatRoom.class, PathInits.DIRECT2);

    public final ListPath<site.bidderown.server.bounded_context.comment.entity.Comment, site.bidderown.server.bounded_context.comment.entity.QComment> comments = this.<site.bidderown.server.bounded_context.comment.entity.Comment, site.bidderown.server.bounded_context.comment.entity.QComment>createList("comments", site.bidderown.server.bounded_context.comment.entity.Comment.class, site.bidderown.server.bounded_context.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> expireAt = createDateTime("expireAt", java.time.LocalDateTime.class);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<site.bidderown.server.bounded_context.image.entity.Image, site.bidderown.server.bounded_context.image.entity.QImage> images = this.<site.bidderown.server.bounded_context.image.entity.Image, site.bidderown.server.bounded_context.image.entity.QImage>createList("images", site.bidderown.server.bounded_context.image.entity.Image.class, site.bidderown.server.bounded_context.image.entity.QImage.class, PathInits.DIRECT2);

    public final EnumPath<ItemStatus> itemStatus = createEnum("itemStatus", ItemStatus.class);

    public final site.bidderown.server.bounded_context.member.entity.QMember member;

    public final NumberPath<Integer> minimumPrice = createNumber("minimumPrice", Integer.class);

    public final ListPath<site.bidderown.server.bounded_context.notification.entity.Notification, site.bidderown.server.bounded_context.notification.entity.QNotification> notifications = this.<site.bidderown.server.bounded_context.notification.entity.Notification, site.bidderown.server.bounded_context.notification.entity.QNotification>createList("notifications", site.bidderown.server.bounded_context.notification.entity.Notification.class, site.bidderown.server.bounded_context.notification.entity.QNotification.class, PathInits.DIRECT2);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QItem(String variable) {
        this(Item.class, forVariable(variable), INITS);
    }

    public QItem(Path<? extends Item> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItem(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItem(PathMetadata metadata, PathInits inits) {
        this(Item.class, metadata, inits);
    }

    public QItem(Class<? extends Item> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new site.bidderown.server.bounded_context.member.entity.QMember(forProperty("member")) : null;
    }

}


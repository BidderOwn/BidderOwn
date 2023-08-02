package site.bidderown.server.boundedcontext.item.entity;

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

    private static final long serialVersionUID = 577162103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItem item = new QItem("item");

    public final site.bidderown.server.base.baseentity.QBaseEntity _super = new site.bidderown.server.base.baseentity.QBaseEntity(this);

    public final ListPath<site.bidderown.server.boundedcontext.bid.entity.Bid, site.bidderown.server.boundedcontext.bid.entity.QBid> bids = this.<site.bidderown.server.boundedcontext.bid.entity.Bid, site.bidderown.server.boundedcontext.bid.entity.QBid>createList("bids", site.bidderown.server.boundedcontext.bid.entity.Bid.class, site.bidderown.server.boundedcontext.bid.entity.QBid.class, PathInits.DIRECT2);

    public final ListPath<site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom, site.bidderown.server.boundedcontext.chatroom.entity.QChatRoom> chatRooms = this.<site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom, site.bidderown.server.boundedcontext.chatroom.entity.QChatRoom>createList("chatRooms", site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom.class, site.bidderown.server.boundedcontext.chatroom.entity.QChatRoom.class, PathInits.DIRECT2);

    public final ListPath<site.bidderown.server.boundedcontext.comment.entity.Comment, site.bidderown.server.boundedcontext.comment.entity.QComment> comments = this.<site.bidderown.server.boundedcontext.comment.entity.Comment, site.bidderown.server.boundedcontext.comment.entity.QComment>createList("comments", site.bidderown.server.boundedcontext.comment.entity.Comment.class, site.bidderown.server.boundedcontext.comment.entity.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> expireAt = createDateTime("expireAt", java.time.LocalDateTime.class);

    public final ListPath<site.bidderown.server.boundedcontext.heart.entity.Heart, site.bidderown.server.boundedcontext.heart.entity.QHeart> hearts = this.<site.bidderown.server.boundedcontext.heart.entity.Heart, site.bidderown.server.boundedcontext.heart.entity.QHeart>createList("hearts", site.bidderown.server.boundedcontext.heart.entity.Heart.class, site.bidderown.server.boundedcontext.heart.entity.QHeart.class, PathInits.DIRECT2);

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final ListPath<site.bidderown.server.boundedcontext.image.entity.Image, site.bidderown.server.boundedcontext.image.entity.QImage> images = this.<site.bidderown.server.boundedcontext.image.entity.Image, site.bidderown.server.boundedcontext.image.entity.QImage>createList("images", site.bidderown.server.boundedcontext.image.entity.Image.class, site.bidderown.server.boundedcontext.image.entity.QImage.class, PathInits.DIRECT2);

    public final EnumPath<ItemStatus> itemStatus = createEnum("itemStatus", ItemStatus.class);

    public final site.bidderown.server.boundedcontext.member.entity.QMember member;

    public final NumberPath<Integer> minimumPrice = createNumber("minimumPrice", Integer.class);

    public final ListPath<site.bidderown.server.boundedcontext.notification.entity.Notification, site.bidderown.server.boundedcontext.notification.entity.QNotification> notifications = this.<site.bidderown.server.boundedcontext.notification.entity.Notification, site.bidderown.server.boundedcontext.notification.entity.QNotification>createList("notifications", site.bidderown.server.boundedcontext.notification.entity.Notification.class, site.bidderown.server.boundedcontext.notification.entity.QNotification.class, PathInits.DIRECT2);

    public final StringPath thumbnailImageFileName = createString("thumbnailImageFileName");

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
        this.member = inits.isInitialized("member") ? new site.bidderown.server.boundedcontext.member.entity.QMember(forProperty("member")) : null;
    }

}


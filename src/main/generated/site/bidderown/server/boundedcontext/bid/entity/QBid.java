package site.bidderown.server.boundedcontext.bid.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBid is a Querydsl query type for Bid
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBid extends EntityPathBase<Bid> {

    private static final long serialVersionUID = 1965451863L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBid bid = new QBid("bid");

    public final site.bidderown.server.base.baseentity.QBaseEntity _super = new site.bidderown.server.base.baseentity.QBaseEntity(this);

    public final site.bidderown.server.boundedcontext.member.entity.QMember bidder;

    public final EnumPath<BidResult> bidResult = createEnum("bidResult", BidResult.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final site.bidderown.server.boundedcontext.item.entity.QItem item;

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBid(String variable) {
        this(Bid.class, forVariable(variable), INITS);
    }

    public QBid(Path<? extends Bid> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBid(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBid(PathMetadata metadata, PathInits inits) {
        this(Bid.class, metadata, inits);
    }

    public QBid(Class<? extends Bid> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bidder = inits.isInitialized("bidder") ? new site.bidderown.server.boundedcontext.member.entity.QMember(forProperty("bidder")) : null;
        this.item = inits.isInitialized("item") ? new site.bidderown.server.boundedcontext.item.entity.QItem(forProperty("item"), inits.get("item")) : null;
    }

}


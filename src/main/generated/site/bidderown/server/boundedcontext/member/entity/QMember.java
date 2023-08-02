package site.bidderown.server.boundedcontext.member.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -848733115L;

    public static final QMember member = new QMember("member1");

    public final site.bidderown.server.base.baseentity.QBaseEntity _super = new site.bidderown.server.base.baseentity.QBaseEntity(this);

    public final ListPath<site.bidderown.server.boundedcontext.bid.entity.Bid, site.bidderown.server.boundedcontext.bid.entity.QBid> bids = this.<site.bidderown.server.boundedcontext.bid.entity.Bid, site.bidderown.server.boundedcontext.bid.entity.QBid>createList("bids", site.bidderown.server.boundedcontext.bid.entity.Bid.class, site.bidderown.server.boundedcontext.bid.entity.QBid.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}


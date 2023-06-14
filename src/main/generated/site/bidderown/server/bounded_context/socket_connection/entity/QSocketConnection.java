package site.bidderown.server.bounded_context.socket_connection.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSocketConnection is a Querydsl query type for SocketConnection
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSocketConnection extends EntityPathBase<SocketConnection> {

    private static final long serialVersionUID = 294885263L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSocketConnection socketConnection = new QSocketConnection("socketConnection");

    public final site.bidderown.server.base.base_entity.QBaseEntity _super = new site.bidderown.server.base.base_entity.QBaseEntity(this);

    public final NumberPath<Long> connectionId = createNumber("connectionId", Long.class);

    public final EnumPath<ConnectionType> connectionType = createEnum("connectionType", ConnectionType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final site.bidderown.server.bounded_context.member.entity.QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSocketConnection(String variable) {
        this(SocketConnection.class, forVariable(variable), INITS);
    }

    public QSocketConnection(Path<? extends SocketConnection> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSocketConnection(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSocketConnection(PathMetadata metadata, PathInits inits) {
        this(SocketConnection.class, metadata, inits);
    }

    public QSocketConnection(Class<? extends SocketConnection> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new site.bidderown.server.bounded_context.member.entity.QMember(forProperty("member")) : null;
    }

}


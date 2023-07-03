package site.bidderown.server.bounded_context.item.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemBase is a Querydsl query type for ItemBase
 */
@Generated("com.querydsl.codegen.DefaultSupertypeSerializer")
public class QItemBase extends EntityPathBase<ItemBase> {

    private static final long serialVersionUID = 1747547197L;

    public static final QItemBase itemBase = new QItemBase("itemBase");

    public final site.bidderown.server.base.base_entity.QBaseEntity _super = new site.bidderown.server.base.base_entity.QBaseEntity(this);

    public final NumberPath<Integer> bidCount = createNumber("bidCount", Integer.class);

    public final NumberPath<Integer> commentCount = createNumber("commentCount", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QItemBase(String variable) {
        super(ItemBase.class, forVariable(variable));
    }

    public QItemBase(Path<? extends ItemBase> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemBase(PathMetadata metadata) {
        super(ItemBase.class, metadata);
    }

}


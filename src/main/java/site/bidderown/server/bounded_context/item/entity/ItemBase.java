package site.bidderown.server.bounded_context.item.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.persistence.MappedSuperclass;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class ItemBase extends BaseEntity {
    private int bidCount;
    private int commentCount;

    public void increaseBidCount() {
        this.bidCount++;
    }

    public void decreaseBidCount() {
        this.bidCount--;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }
}

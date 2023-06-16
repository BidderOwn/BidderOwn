package site.bidderown.server.bounded_context.image.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Image extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    private String fileName;

    @Builder
    public Image(Item item, String fileName) {
        this.fileName = fileName;
        addImage(item);
    }

    public static Image of(Item item, String fileName) {
        return new Image(item, fileName);
    }

    private void addImage(Item item) {
        this.item = item;
        this.item.getImages().add(this);
    }
}

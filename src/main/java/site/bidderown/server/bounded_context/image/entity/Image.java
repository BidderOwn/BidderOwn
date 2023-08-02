package site.bidderown.server.bounded_context.image.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Image extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    private String fileName;

    public static Image of(Item item, String fileName) {
        return Image.builder()
                .item(item)
                .fileName(fileName)
                .build();
    }
}

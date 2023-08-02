package site.bidderown.server.bounded_context.heart.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Heart extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    public static Heart of(Member member, Item item) {
        return Heart.builder()
                .member(member)
                .item(item)
                .build();
    }
}

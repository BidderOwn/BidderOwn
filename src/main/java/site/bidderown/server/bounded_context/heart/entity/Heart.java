package site.bidderown.server.bounded_context.heart.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.heart.entitylistener.HeartEntityListener;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(value = HeartEntityListener.class)
public class Heart extends BaseEntity {

    /**
     * 상품 좋아요 클릭
     * 알림 -> 판매자
     * 상품 좋아요 +1
     * 좋아요한사람 마이페이지 -> 즐겨찾기 품목
     */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    @Builder
    private Heart (
            Member member,
            Item item
    ) {
        this.member = member;
        this.item = item;
        item.getHearts().add(this);
    }
}

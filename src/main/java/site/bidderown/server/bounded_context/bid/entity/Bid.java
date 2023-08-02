package site.bidderown.server.bounded_context.bid.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.bid.entitylistener.BidEntityListener;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(BidEntityListener.class)
public class Bid extends BaseEntity {
  
    private int price;

    @Enumerated(value = EnumType.STRING)
    private BidResult bidResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member bidder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Item item;

    public static Bid of(int price, Member bidder, Item item){
        return new Bid(price, BidResult.WAIT, bidder, item);
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateBidResultFail() {
        this.bidResult = BidResult.FAIL;
    }
}

package site.bidderown.server.boundedcontext.bid.entity;

import lombok.*;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.bid.entitylistener.BidEntityListener;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

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
        Bid bid = Bid.builder()
                .price(price)
                .bidResult(BidResult.WAIT)
                .bidder(bidder)
                .item(item)
                .build();
        item.getBids().add(bid);
        return bid;
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateBidResultFail() {
        this.bidResult = BidResult.FAIL;
    }
}

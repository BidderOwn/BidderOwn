package site.bidderown.server.bounded_context.bid.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
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

    @Builder
    private Bid(int price, BidResult bidResult, Member bidder, Item item) {
        this.price = price;
        this.bidResult = bidResult;
        this.bidder = bidder;
        this.item = item;
        item.getBids().add(this);
    }

    public static Bid of(BidRequest bidRequest, Member bidder, Item item){
        return Bid.builder()
                .price(bidRequest.getItemPrice())
                .bidResult(BidResult.WAIT)
                .bidder(bidder)
                .item(item)
                .build();
    }

    public static Bid of(int price, Member bidder, Item item){
        return Bid.builder()
                .price(price)
                .bidResult(BidResult.WAIT)
                .bidder(bidder)
                .item(item)
                .build();
    }

    public void updatePrice(int price) {
        this.price = price;
    }

    public void updateBidResultFail() {
        this.bidResult = BidResult.FAIL;
    }
}

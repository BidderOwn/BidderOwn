package site.bidderown.server.bounded_context.bid.entity;

import lombok.*;
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member bidder;

    @ManyToOne
    @JoinColumn
    private Item item;

    @Builder
    private Bid(int price, BidResult bidResult, Member bidder, Item item) {
        this.price = price;
        this.bidResult = bidResult;
        this.bidder = bidder;
        this.item = item;
    }

    public static Bid of(BidRequest bidRequest, Member bidder, Item item){
        return Bid.builder()
                .price(bidRequest.getItemPrice())
                .bidResult(BidResult.WAIT)
                .bidder(bidder)
                .item(item)
                .build();
    }
}
package site.bidderown.server.boundedcontext.bid.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;

import java.util.List;
import java.util.stream.Collectors;

import static site.bidderown.server.boundedcontext.bid.entity.QBid.bid;
import static site.bidderown.server.boundedcontext.item.entity.QItem.item;
import static site.bidderown.server.boundedcontext.member.entity.QMember.member;

@RequiredArgsConstructor
@Repository
public class BidCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Long> findBidItemIds(String memberName) {
        List<Bid> bids = jpaQueryFactory.select(bid)
                .from(bid)
                .join(bid.bidder, member)
                .join(bid.item, item)
                .where(
                        eqToMember(memberName),
                        eqToItemStatus()
                )
                .fetch();

        return bids.stream()
                .map(bid -> bid.getItem().getId())
                .collect(Collectors.toList());
    }

    private BooleanExpression eqToMember(String memberName) {
        return member.name.eq(memberName);
    }

    private BooleanExpression eqToItemStatus() {
        return item.itemStatus.eq(ItemStatus.BIDDING);
    }
}

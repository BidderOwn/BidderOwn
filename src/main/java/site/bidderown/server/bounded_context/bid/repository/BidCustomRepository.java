package site.bidderown.server.bounded_context.bid.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.dto.BidEndNotification;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static site.bidderown.server.bounded_context.bid.entity.QBid.bid;
import static site.bidderown.server.bounded_context.item.entity.QItem.item;
import static site.bidderown.server.bounded_context.member.entity.QMember.member;

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

    public List<BidEndNotification> findBidByItemExpireAt(Pageable pageable) {
        return jpaQueryFactory
                .select(Projections.constructor(
                            BidEndNotification.class,
                            item.id,
                            bid.bidder.id))
                .from(bid)
                .join(bid.item, item)
                .where(betweenCurrentTime())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression eqToMember(String memberName) {
        return member.name.eq(memberName);
    }

    private BooleanExpression eqToItemStatus() {
        return item.itemStatus.eq(ItemStatus.BIDDING);
    }

    private BooleanExpression betweenCurrentTime() {
        LocalDateTime start = TimeUtils.getCurrentOClock();
        LocalDateTime end = TimeUtils.getCurrentOClockPlus(1);

        return item.createdAt.between(start, end); // TODO expireAt으로 변경
    }
}

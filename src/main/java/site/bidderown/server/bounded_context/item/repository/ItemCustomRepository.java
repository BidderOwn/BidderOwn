package site.bidderown.server.bounded_context.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.controller.dto.ItemListResponse;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static site.bidderown.server.bounded_context.bid.entity.QBid.bid;
import static site.bidderown.server.bounded_context.item.entity.QItem.item;

@RequiredArgsConstructor
@Repository
public class ItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    public List<Item> paginationNoOffsetBuilder(Long itemId, int pageSize) {
        return queryFactory
                .selectFrom(item)
                .where(
                        ltItemId(itemId),
                        betweenCurrentTime()
                )
                .orderBy(item.id.desc())
                .limit(pageSize)
                .fetch();
    }

    public List<ItemListResponse> findAll(int sortCode, String searchText, Pageable pageable) {
        List<Item> items = queryFactory.selectFrom(item)
                .orderBy(orderBySortCode(sortCode))
                .where(eqToSearchText(searchText))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return items.stream()
                .map(item -> ItemListResponse.of(
                        item,
                        minItemPrice(item.getId()),
                        maxItemPrice(item.getId()))
                )
                .collect(Collectors.toList());
    }

    public Integer minItemPrice(Long itemId) {
        return queryFactory.select(bid.price.min())
                .where(item.id.eq(itemId))
                .from(bid)
                .fetchOne();
    }

    public Integer maxItemPrice(Long itemId) {
        return queryFactory.select(bid.price.max())
                .where(item.id.eq(itemId))
                .from(bid)
                .fetchOne();
    }

    public Integer avgItemPrice(Long itemId) {
        Double avg = queryFactory.select(bid.price.avg())
                .where(item.id.eq(itemId))
                .from(bid)
                .fetchOne();
        if (avg != null) return avg.intValue();
        return null;
    }

    private OrderSpecifier<?>[] orderBySortCode(int sortCode) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        switch (sortCode) {
            case 2 -> orderSpecifiers.add(item.bids.size().desc());
            case 3 -> orderSpecifiers.add(item.expireAt.asc());
        }
        orderSpecifiers.add(item.id.desc());
        orderSpecifiers.add(item.itemStatus.desc());
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private BooleanExpression eqToSearchText(String searchText) {
        if (StringUtils.isEmpty(searchText)) return null;
        StringExpression keywordExpression = Expressions.asString("%" + searchText + "%");
        return eqToTitle(keywordExpression)
                .or(eqToDescription(keywordExpression))
                .or(eqToSeller(keywordExpression));
    }

    private BooleanExpression eqToTitle(StringExpression searchText) {
        return item.title.like(searchText);
    }

    private BooleanExpression eqToDescription(StringExpression searchText) {
        return item.description.like(searchText);
    }

    private BooleanExpression eqToSeller(StringExpression searchText) {
        return item.member.name.like(searchText);
    }

    private BooleanExpression betweenCurrentTime() {
        LocalDateTime start = TimeUtils.getCurrentOClock();
        LocalDateTime end = TimeUtils.getCurrentOClockPlus(1);

        return item.createdAt.between(start, end); // TODO expireAt으로 변경
    }

    private BooleanExpression ltItemId(Long itemId) {
        if (itemId == null) {
            return null;
        }

        return item.id.lt(itemId);
    }
}

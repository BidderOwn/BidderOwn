package site.bidderown.server.boundedcontext.item.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemsResponse;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static site.bidderown.server.boundedcontext.bid.entity.QBid.bid;
import static site.bidderown.server.boundedcontext.item.entity.QItem.item;

@RequiredArgsConstructor
@Repository
public class ItemCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<ItemsResponse> findItemsLegacy(Pageable pageable, int sortCode, String searchText, boolean isAll) {
        return queryFactory
                .select(Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                item.bids.size(),
                                item.comments.size(),
                                item.hearts.size(),
                                item.thumbnailImageFileName,
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(
                        eqToSearchText(searchText),
                        eqNotDeleted(),
                        eqAll(isAll)
                )
                .orderBy(orderBySortCode(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<ItemsResponse> findItemsSortByIdDesc(Long lastItemId, String searchText, boolean isAll, int pageSize) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                item.bids.size(),
                                item.comments.size(),
                                item.hearts.size(),
                                item.thumbnailImageFileName,
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(
                        ltItemId(lastItemId),
                        eqToSearchText(searchText),
                        eqNotDeleted(),
                        eqAll(isAll)
                )
                .orderBy(item.id.desc())
                .limit(pageSize)
                .fetch();
    }

    public List<ItemsResponse> findItemsSortByExpireAt(String searchText, boolean isAll, Pageable pageable) {
        List<Long> ids = queryFactory
                .select(item.id)
                .from(item)
                .where(
                        eqToSearchText(searchText),
                        eqNotDeleted(),
                        eqAll(isAll)
                )
                .orderBy(
                        item.expireAt.asc(),
                        item.itemStatus.asc()
                )
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        if (CollectionUtils.isEmpty(ids)) return new ArrayList<>();

        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                item.bids.size(),
                                item.comments.size(),
                                item.hearts.size(),
                                item.thumbnailImageFileName,
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(item.id.in(ids))
                .fetch();
    }

    public List<ItemsResponse> findItemsInIdsSortByPopularity(List<Long> ids) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                item.bids.size(),
                                item.comments.size(),
                                item.hearts.size(),
                                item.thumbnailImageFileName,
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(item.id.in(ids))
                .fetch();
    }

    public List<Long> findItemIdsSortByPopularity(Pageable pageable, String searchText) {
        return queryFactory
                .select(item.id)
                .from(item)
                .leftJoin(item.bids, bid)
                .where(
                        eqToSearchText(searchText),
                        eqNotDeleted()
                )
                .groupBy(item.id)
                .orderBy(bid.count().desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    /**
     * @param id 상품 아이디
     * @return Optional로 받아서 404 처리
     * @description 상품 ItemDetailResponse로 받음
     */
    public Optional<ItemDetailResponse> findItemById(Long id) {
        return Optional.ofNullable(
                queryFactory.select(
                                Projections.constructor(
                                        ItemDetailResponse.class,
                                        item.id,
                                        item.member.id,
                                        item.title,
                                        item.description,
                                        item.member.name,
                                        item.minimumPrice,
                                        item.thumbnailImageFileName,
                                        item.bids.size(),
                                        item.comments.size(),
                                        item.hearts.size(),
                                        item.itemStatus,
                                        item.expireAt
                                )
                        )
                        .from(item)
                        .where(
                                item.id.eq(id),
                                eqNotDeleted()
                        )
                        .fetchOne()
        );
    }

    public Integer findItemBidMaxPriceByItemId(Long itemId) {
        return queryFactory
                .select(bid.price.max())
                .from(bid)
                .where(bid.item.id.eq(itemId))
                .fetchOne();
    }

    public Integer findItemBidMinPriceByItemId(Long itemId) {
        return queryFactory
                .select(bid.price.min())
                .from(bid)
                .where(bid.item.id.eq(itemId))
                .fetchOne();
    }

    public List<Long> findIdsByExpiredItems() {
        return queryFactory
                .select(item.id)
                .from(item)
                .where(
                        ltExpiredAt(LocalDateTime.now()),
                        eqBidding()
                )
                .fetch();
    }

    private BooleanExpression eqBidding() {
        return item.itemStatus.eq(ItemStatus.BIDDING);
    }

    private BooleanExpression ltExpiredAt(LocalDateTime now) {
        return item.expireAt.lt(now);
    }

    private BooleanExpression ltItemId(Long itemId) {
        if (itemId == null) {
            return null;
        }
        return item.id.lt(itemId);
    }

    private BooleanExpression eqAll(boolean isAll) {
        if (isAll) return null;
        return item.itemStatus.eq(ItemStatus.BIDDING);
    }

    private OrderSpecifier<?>[] orderBySortCode(int sortCode) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
        switch (sortCode) {
            case 2 -> orderSpecifiers.add(item.bids.size().desc());
            case 3 -> orderSpecifiers.add(item.expireAt.asc());
        }
        orderSpecifiers.add(item.id.desc());
        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

    private BooleanExpression eqToSearchText(String searchText) {
        if (StringUtils.isEmpty(searchText)) return null;
        StringExpression keywordExpression = Expressions.asString("%" + searchText + "%");
        return eqToTitle(keywordExpression)
                .or(eqToDescription(keywordExpression))
                .or(eqToSeller(keywordExpression));
    }

    private BooleanExpression eqNotDeleted() {
        return item.deleted.eq(false);
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
}

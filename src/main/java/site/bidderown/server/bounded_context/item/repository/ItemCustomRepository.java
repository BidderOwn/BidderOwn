package site.bidderown.server.bounded_context.item.repository;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemsResponse;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static site.bidderown.server.bounded_context.bid.entity.QBid.bid;
import static site.bidderown.server.bounded_context.image.entity.QImage.image;
import static site.bidderown.server.bounded_context.item.entity.QItem.item;

@RequiredArgsConstructor
@Repository
public class ItemCustomRepository {
    private final JPAQueryFactory queryFactory;

    /**
     * @description  offset 적용하지 않고 가져오는 법, 성능 향상을 위해 남겨둠
     */
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

    /**
     * @param sortCode   정렬 기준, 1: 최신순 / 2: 인기순 / 3: 경매 마감순
     * @param searchText 검색어(제목, 내용, 작성자)
     * @param pageable   페이징: 9
     * @return 홈화면에 보여질 아이템 리스트
     * @description https://www.notion.so/eui9179/20230-06-20-90075e3fdf484754843adcae04134f76?pvs=4
     */
    public List<ItemsResponse> findItems(int sortCode, String searchText, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                itemBidMaxPrice(),
                                itemBidMinPrice(),
                                item.comments.size(),
                                item.bids.size(),
                                image.fileName,
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .leftJoin(item.images, image)
                .on(image.id.eq(itemThumbnailImageMaxId()))
                .where(
                        eqToSearchText(searchText),
                        eqNotDeleted()
                )
                .orderBy(orderBySortCode(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    /**
     * @description 성능 테스트를 위해 남겨둔 메서드입니다. findItems()를 사용하시면 됩니다.
     */
    public List<ItemsResponse> findItems_no_dsl(int sortCode, String searchText, Pageable pageable) {
        List<Item> items = queryFactory.selectFrom(item)
                .where(eqToSearchText(searchText))
                .orderBy(orderBySortCode(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return items.stream()
                .map(item -> ItemsResponse.of(
                        item,
                        minItemPrice(item.getId()),
                        maxItemPrice(item.getId()))
                )
                .collect(Collectors.toList());
    }

    public List<ItemsResponse> findItems_dsl_page(int sortCode, String searchText, Pageable pageable) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                itemBidMaxPrice(),
                                itemBidMinPrice(),
                                item.comments.size(),
                                item.bids.size(),
                                itemThumbnailImageFileName(),
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(
                        eqToSearchText(searchText),
                        eqNotDeleted()
                )
                .orderBy(orderBySortCode(sortCode))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<ItemsResponse> findItems_dsl_no_Offset(Long lastItemId, int sortCode, String searchText, int pageSize) {
        return queryFactory
                .select(
                        Projections.constructor(
                                ItemsResponse.class,
                                item.id,
                                item.title,
                                item.minimumPrice,
                                itemBidMaxPrice(),
                                itemBidMinPrice(),
                                item.comments.size(),
                                item.bids.size(),
                                itemThumbnailImageFileName(),
                                item.itemStatus,
                                item.expireAt
                        )
                )
                .from(item)
                .where(
                        eqNotDeleted(),
                        ltItemId(lastItemId),
                        eqToSearchText(searchText)
                )
                .orderBy(orderBySortCode(sortCode))
                .limit(pageSize)
                .fetch();
    }

    /**
     * @param id 상품 아이디
     * @description 상품 ItemDetailResponse로 받음
     * @return Optional로 받아서 404 처리
     */
    public Optional<ItemDetailResponse> findItemById(Long id) {
        return Optional.ofNullable(queryFactory.select(
                        Projections.constructor(
                                ItemDetailResponse.class,
                                item.id,
                                item.member.id,
                                item.title,
                                item.description,
                                item.member.name,
                                item.minimumPrice,
                                itemBidMaxPrice(),
                                itemBidMinPrice(),
                                image.fileName,
                                item.bids.size(),
                                item.itemStatus,
                                item.expireAt
                        ))
                .from(item)
                .leftJoin(item.images, image)
                .on(image.id.eq(itemThumbnailImageMaxId()))
                .where(item.id.eq(id), eqNotDeleted())
                .fetchOne());
    }

    private BooleanExpression ltItemId(Long itemId) {
        if (itemId == null) {
            return null;
        }

        return item.id.lt(itemId);

    }

    private Expression<Long> itemThumbnailImageMaxId() {
        return JPAExpressions.select(image.id.min())
                .from(image)
                .where(image.item.eq(item));
    }

    private Expression<String> itemThumbnailImageFileName() {
        return JPAExpressions.select(image.fileName)
                .from(image)
                .where(image.id.eq(itemThumbnailImageMaxId()));
    }

    private Expression<Integer> itemBidMaxPrice() {
        return JPAExpressions.select(bid.price.max())
                .from(bid)
                .where(bid.item.eq(item));
    }

    private Expression<Integer> itemBidMinPrice() {
        return JPAExpressions.select(bid.price.min())
                .from(bid)
                .where(bid.item.eq(item));
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

    private BooleanExpression betweenCurrentTime() {
        LocalDateTime start = TimeUtils.getCurrentOClock();
        LocalDateTime end = TimeUtils.getCurrentOClockPlus(1);

        return item.createdAt.between(start, end); // TODO expireAt으로 변경
    }
}

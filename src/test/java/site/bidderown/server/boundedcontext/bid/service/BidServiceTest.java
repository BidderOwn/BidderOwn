package site.bidderown.server.boundedcontext.bid.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.BidEndItemException;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidRequest;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidResponse;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.bid.repository.BidRepository;
import site.bidderown.server.boundedcontext.image.service.ImageService;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemRequest;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.repository.ItemRepository;
import site.bidderown.server.boundedcontext.item.service.ItemRedisService;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class BidServiceTest {
    @Autowired
    BidService bidService;

    @Autowired
    BidRepository bidRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    MemberService memberService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemRedisService itemRedisService;

    @Autowired
    ImageService imageService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    @DisplayName("입찰 생성 1")
    void t01() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        int price = 10000;

        Item item = createItem(seller, "test1","test1", price);

        //when
        Long bidId = bidService.create(price, item, bidder);

        //then
        Bid bid = bidService.getBid(bidId);
        assertThat(bid.getBidder().getName()).isEqualTo(bidder.getName());
    }

    @Test
    @DisplayName("입찰 생성 2")
    void t02() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        int price = 100000;

        //when
        Long bidId = bidService.create(price, item, bidder);

        //then
        Bid bid = bidService.getBid(bidId);
        assertThat(bid.getId()).isEqualTo(bidId);
    }

    /**
     * 자신의 상품에는 입찰을 생성할 수 없습니다.
     * item.member 의 이름과 입찰자의 이름이 같으면 입찰을 할 수 없습니다.
     */
    @Test
    @DisplayName("입찰 생성 예외_자기 상품 입찰 금지")
    void t03() {
        //given
        Member seller = createUser("seller");
        int price = 10000;
        Item item = createItem(seller, "test1","test1", price);

        //when
        Throwable exception = Assertions.assertThrows(
                ForbiddenException.class,
                () -> bidService.create(price, item, seller)
        );

        //then
        assertThat(exception.getMessage().contains("자신의 상품에는 입찰을 할 수 없습니다.")).isTrue();

    }

    /**
     * handleBid()로 전반적인 입찰 과정을 진행합니다.
     */
    @Test
    @DisplayName("입찰 하기")
    void t04() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        BidRequest bidRequest = BidRequest.of(item.getId(), 10000);

        //when
        Long bidTestId = bidService.handleBid(bidRequest, bidder.getName());

        //then
        Long bidId = bidRepository.findByItemAndBidder(item, bidder).get().getId();
        assertThat(bidTestId).isEqualTo(bidId);

    }

    /**
     * 입찰이 존재하는 경우 입찰가격만 업데이트합니다.
     * 원래 bidId와 새로 입찰한 bidId가 동일한지 확인합니다.
     * 동일하면 입찰가격이 새로 업데이트됐는지 확인합니다.
     */
    @Test
    @DisplayName("입찰이 존재하는 경우 입찰가격만 업데이트한다.")
    void t05() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);

        Bid bid = createBid(bidder, item);
        Long bidId = bid.getId();
        int bidPrice = bid.getPrice();

        BidRequest bidRequest = BidRequest.of(item.getId(), 20000);

        //when
        Long bidTestId = bidService.handleBid(bidRequest, bidder.getName());

        //then
        assertThat(bidTestId).isEqualTo(bidId);
        assertThat(bidRepository.findById(bidId).get().getPrice()).isNotEqualTo(bidPrice).isEqualTo(20000);
    }

    /**
     * itemStatus가 BID_END 또는 SOLDOUT이면 입찰을 할 수 없습니다.
     * BidEndItemException이 발생합니다.
     */
    @Test
    @DisplayName("상품이 경매종료 or 판매완료 상태일 경우 입찰 불가 예외 처리")
    void t06() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        itemService.soldOut(item.getId(), seller.getName());
        BidRequest bidRequest = BidRequest.of(item.getId(), 10000);

        //when
        Throwable exception = Assertions.assertThrows(
                BidEndItemException.class,
                () -> bidService.handleBid(bidRequest, bidder.getName())
        );

        //then
        assertThat(exception.getMessage().contains("입찰이 종료된 아이템입니다.")).isTrue();
    }

    /**
     * bidId로 입찰을 조회합니다.
     */
    @Test
    @DisplayName("입찰ID로 입찰 조회")
    void t07() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        Bid bid = createBid(bidder, item);

        //when
        Bid bidTest = bidService.getBid(bid.getId());

        //then
        assertThat(bidTest.getId()).isEqualTo(bid.getId());

    }

    /**
     * itemId을 사용하여 item에 있는 모든 입찰을 조회합니다.
     */
    @Test
    @DisplayName("상품ID로 입찰 조회")
    void t08() {
        //given
        Member seller = createUser("seller");
        Member bidder1 = createUser("bidder1");
        Member bidder2 = createUser("bidder2");
        Item item = createItem(seller, "test1","test1", 10000);
        createBid(bidder1, item);
        createBid(bidder2, item);

        //when
        List<BidResponse> bids = bidService.getBids(item.getId());

        //then
        assertThat(bids.size()).isEqualTo(2);

    }

    /**
     * bidId와 bidderName을 사용하여 입찰을 삭제합니다.
     */
    @Test
    @DisplayName("입찰 삭제")
    void t09() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        Bid bid = createBid(bidder, item);

        //when
        bidService.delete(bid.getId(), bidder.getName());

        //then
        Optional<Bid> bidTest = bidRepository.findById(bid.getId());
        assertThat(bidTest).isEmpty();
    }

    /**
     * 권한이 없는 경우 입찰을 삭제할 수 없습니다.
     * bidderName 과 userName이 일치하지 않는 경우
     * ForBiddenException이 발생합니다.
     */
    @Test
    @DisplayName("입찰 삭제 시 권한이 없는 경우 예외 처리")
    void t011() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        Bid bid = createBid(bidder, item);

        //when
        Throwable exception = Assertions.assertThrows(
                ForbiddenException.class,
                () -> bidService.delete(bid.getId(), "fakeUser")
        );

        //then
        assertThat(exception.getMessage().contains("입찰 삭제 권한이 없습니다.")).isTrue();

    }

    /**
     * bidderName을 사용하여 입찰자가 진행한 모든 입찰들을 조회합니다.
     */
    @Test
    @DisplayName("유저가 입찰한 상품ID 리스트 조회")
    void t012() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");

        Item item1 = createItem(seller, "test1","test1", 10000);
        Item item2 = createItem(seller, "test2","test2", 10000);
        Item item3 = createItem(seller, "test3","test3", 10000);

        createBid(bidder, item1);
        createBid(bidder, item2);
        createBid(bidder, item3);

        //when
        List<Long> bidItemIds = bidService.getBidItemIds(bidder.getName());

        //then
        assertThat(bidItemIds.size()).isEqualTo(3);
    }

    Member createUser(String username){
        return memberService.join(username,"1234");
    }

    Item createItem(Member member, String itemTitle, String itemDescription, Integer minimumPrice){
        Item item = itemRepository.save(
                Item.of(ItemRequest.builder()
                                .title(itemTitle)
                                .description(itemDescription)
                                .period(3)
                                .minimumPrice(minimumPrice)
                                .build(), member));
        itemRedisService.createWithExpire(item, 3);
        imageService.create(item, List.of("image1.jpeg"));
        return item;
    }

    Bid createBid(Member bidder, Item item) {
        int price = 10000;
        return bidRepository.save(Bid.of(price, bidder, item));
    }
}
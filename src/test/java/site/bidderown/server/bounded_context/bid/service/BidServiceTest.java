package site.bidderown.server.bounded_context.bid.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

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
    ImageService imageService;

    Member createUser(String username){
        return memberService.join(username,"1234");
    }

    Item createItem(Member member, String itemTitle, String itemDescription, Integer minimumPrice){
        Item item = itemRepository.save(
                Item.of(
                        ItemRequest.builder()
                                .title(itemTitle)
                                .description(itemDescription)
                                .period(3)
                                .minimumPrice(minimumPrice)
                                .build(), member));
        imageService.create(item, List.of("image1.jpeg"));
        return item;
    }

    Bid createBid(Member bidder, Item item) {
        BidRequest bidRequest = BidRequest.builder()
                .itemId(item.getId())
                .itemPrice(10000)
                .build();

        return bidRepository.save(Bid.of(bidRequest, bidder, item));
    }

    @Test
    @DisplayName("입찰 생성 1")
    void t01() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);

        BidRequest bidRequest = BidRequest.builder()
                .itemId(item.getId())
                .itemPrice(10000)
                .build();

        //when
        bidService.create(bidRequest, bidder.getName());

        //then
        Optional<Bid> bid = bidRepository.findByItemAndBidder(item, bidder);
        assertThat(bid.get().getBidder().getName()).isEqualTo(bidder.getName());

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
        Long bidTestId = bidService.create(price, item, bidder);

        //then
        Long bidId = bidRepository.findByItemAndBidder(item, bidder).get().getId();
        assertThat(bidTestId).isEqualTo(bidId);


    }

    @Test
    @DisplayName("입찰 생성 예외_자기 상품 입찰 금지")
    void t03() {
        //given
        Member seller = createUser("seller");
        Item item = createItem(seller, "test1","test1", 10000);

        BidRequest bidRequest = BidRequest.builder()
                .itemId(item.getId())
                .itemPrice(10000)
                .build();

        //when - then
        Assertions.assertThrows(ForbiddenException.class, () -> {
            bidService.create(bidRequest, seller.getName());
        });

    }

    @Test
    @DisplayName("입찰 하기")
    void t04() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        BidRequest bidRequest = BidRequest.builder()
                .itemId(item.getId())
                .itemPrice(10000)
                .build();

        //when
        Long bidTestId = bidService.handleBid(bidRequest, bidder.getName());

        //then
        Long bidId = bidRepository.findByItemAndBidder(item, bidder).get().getId();
        assertThat(bidTestId).isEqualTo(bidId);

    }

    @Test
    @DisplayName("입찰ID로 입찰 조회")
    void t05() {
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

    @Test
    @DisplayName("상품ID로 입찰 조회")
    void t06() {
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

    @Test
    @DisplayName("입찰 삭제")
    void t07() {
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

    @Test
    @DisplayName("입찰 삭제 시 입찰이 없는 경우 예외 처리")
    void t08() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        Bid bid = createBid(bidder, item);

        //when - then
        Assertions.assertThrows(NotFoundException.class, () -> {
            bidService.delete(bid.getId()+1, bidder.getName());
        });
    }

    @Test
    @DisplayName("입찰 삭제 시 권한이 없는 경우 예외 처리")
    void t09() {
        //given
        Member seller = createUser("seller");
        Member bidder = createUser("bidder");
        Item item = createItem(seller, "test1","test1", 10000);
        Bid bid = createBid(bidder, item);

        //when - then
        Assertions.assertThrows(ForbiddenException.class, () -> {
            bidService.delete(bid.getId(), "fakeUser");
        });
    }

    @Test
    @DisplayName("유저가 입찰한 상품ID 리스트 조회")
    void t010() {
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
}
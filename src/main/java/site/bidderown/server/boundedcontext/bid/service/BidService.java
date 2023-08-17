package site.bidderown.server.boundedcontext.bid.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.annotation.DistributedLock;
import site.bidderown.server.base.exception.custom_exception.BidEndItemException;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.WrongBidPriceException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidRequest;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidResponse;
import site.bidderown.server.boundedcontext.bid.controller.dto.BidResponses;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.bid.repository.BidCustomRepository;
import site.bidderown.server.boundedcontext.bid.repository.BidRepository;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;
import site.bidderown.server.boundedcontext.item.service.ItemRedisService;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BidService {
    private final BidRepository bidRepository;
    private final BidCustomRepository bidCustomRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final ItemRedisService itemRedisService;

    /**
     * 입찰가 제시
     * 분산락을 사용하여 동시성 제어
     * 1. 중복 -> 가격 변경
     * 2. 없으면 새로 생성
     * @annotation LOCK:{itemId} 형식으로 lock이름 생성
     */
    @DistributedLock(key = "#bidRequest.getItemId()")
    public Bid handleBid(BidRequest bidRequest, String username) {
        Item item = itemService.getItem(bidRequest.getItemId());

        if (!isBidding(item)) {
            throw new BidEndItemException(item.getId());
        }

        Integer maxPrice = bidRepository.findMaxPrice(item);
        int bidPrice = bidRequest.getItemPrice();

        if (!availableBid(item, maxPrice, bidPrice)) {
            throw new WrongBidPriceException(item.getId());
        }

        Member bidder = memberService.getMember(username);
        Optional<Bid> opBid = bidRepository.findByItemAndBidder(item, bidder);

        if (opBid.isEmpty()) {
            return create(bidPrice, item, bidder);
        }

        Bid bid = opBid.get();
        bid.updatePrice(bidPrice);

        return bid;
    }

    public Bid getBid(Long bidId) {
        return bidRepository.findById(bidId).orElseThrow(() -> new NotFoundException("존재하지 않는 입찰입니다.", bidId + ""));
    }

    @Transactional
    public Bid create(int price, Item item, Member bidder) {
        if (isMyItem(item, bidder.getName())) {
            throw new ForbiddenException("자신의 상품에는 입찰을 할 수 없습니다.");
        }

        Bid bid = Bid.of(price, bidder, item);
        return bidRepository.save(bid);
    }

    public List<BidResponse> getBids(Item item) {
        return bidCustomRepository.findBidsByItem(item).stream()
                .map(BidResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long bidId, String bidderName) {
        Bid bid = getBid(bidId);

        if (!hasAuthorization(bid, bidderName)) {
            throw new ForbiddenException("입찰 삭제 권한이 없습니다.");
        }

        bidRepository.delete(bid);
    }

    @Transactional(readOnly = true)
    public BidResponses getBidListWithMaxPrice(Long itemId) {
        Item item = itemService.getItem(itemId);
        Integer maxPrice = bidRepository.findMaxPrice(item);
        List<BidResponse> bids = getBids(item);

        return BidResponses.of(maxPrice, bids);
    }

    public List<Long> getBidItemIds(String username) {
        return bidCustomRepository.findBidItemIds(username);
    }

    private boolean hasAuthorization(Bid bid, String bidderName) {
        return bid.getBidder().getName().equals(bidderName);
    }

    private boolean isMyItem(Item item, String bidderName) {
        return item.getMember().getName().equals(bidderName);
    }

    private boolean isBidding(Item item) {
        return (item.getItemStatus().equals(ItemStatus.BIDDING)) &&
                (itemRedisService.containsKey(item));
    }

    /**
     * 입찰 가격 기준
     * 1. 기본 상품 값보다 커야합니다.
     * 2. 최근에 입찰가가 제시된 가격보다 높아야 합니다.
     * 3. 입찰가 증가는 원래 상품 가격의 10%보다 클 수 없습니다.
     */
    private boolean availableBid(Item item, Integer maxPrice, int bidPrice) {
        if (Objects.isNull(maxPrice)) {
            return item.getMinimumPrice() < bidPrice &&
                    (item.getMinimumPrice() / 10) >= bidPrice - item.getMinimumPrice();
        }
        return maxPrice < bidPrice &&
                (item.getMinimumPrice() / 10) >= bidPrice - maxPrice;
    }
}
package site.bidderown.server.bounded_context.bid.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.BidEndItemException;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidCustomRepository;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final BidCustomRepository bidCustomRepository;
    private final MemberService memberService;
    private final ItemService itemService;
    private final ItemRedisService itemRedisService;

    @Transactional
    public void create(BidRequest bidRequest, String username) {
        Item item = itemService.getItem(bidRequest.getItemId());

        if (isMyItem(item, username)) {
            throw new ForbiddenException("자신의 상품에는 입찰을 할 수 없습니다.");
        }

        Member member = memberService.getMember(username);
        bidRepository.save(Bid.of(bidRequest, member, item));
    }

    /**
     * @description 입찰가 제시
     * 1. 중복 -> 가격 변경
     * 2. 없으면 새로 생성
     */
    @Transactional
    public Long handleBid(BidRequest bidRequest, String username) {
        Item item = itemService.getItem(bidRequest.getItemId());

        if (!availableBid(item)) {
            throw new BidEndItemException("입찰이 종료된 아이템입니다.", item.getId() + "");
        }

        Member bidder = memberService.getMember(username);
        Optional<Bid> opBid = bidRepository.findByItemAndBidder(item, bidder);

        if (opBid.isEmpty()) {
            return create(bidRequest.getItemPrice(), item, bidder);
        }

        Bid bid = opBid.get();
        bid.updatePrice(bidRequest.getItemPrice());

        return bid.getId();
    }

    public Bid getBid(Long bidId){
        return  bidRepository.findById(bidId).orElseThrow(() -> new NotFoundException("존재하지 않는 입찰입니다.", bidId + ""));
    }

    public List<Bid> getBidsAfter(LocalDateTime createdAt) {
        return bidRepository.findBidsByCreatedAtAfter(createdAt);
    }

    @Transactional
    public Long create(int price, Item item, Member bidder) {
        if(isMyItem(item, bidder.getName())) {
            throw new ForbiddenException("자신의 상품에는 입찰을 할 수 없습니다.");
        }
        Bid bid = Bid.of(price, bidder, item);
        return bidRepository.save(bid).getId();
    }

    public List<BidResponse> getBids(Long itemId) {
        Item item = itemService.getItem(itemId);

        return bidRepository.findByItemOrderByUpdatedAtDesc(item).stream()
                .map(bid -> BidResponse.of(bid, item)).collect(Collectors.toList());
    }

    public void clear() {
        bidRepository.deleteAll();
    }

    public void delete(Long bidId, String bidderName) {

        Bid findBid = bidRepository.findById(bidId)
                .orElseThrow(() -> new NotFoundException("입찰이 없습니다.", bidId + ""));
        if(!hasAuthorization(findBid, bidderName)) {
            throw new ForbiddenException("입찰 삭제 권한이 없습니다.");
        }

        bidRepository.delete(findBid);
    }

    public BidDetails getBidStatistics(Long itemId) {
        Item item = itemService.getItem(itemId);
        return BidDetails.of(item, bidRepository.findMaxPrice(item), bidRepository.findMinPrice(item), bidRepository.findAvgPrice(item));
    }

    public List<Long> getBidItemIds(String username) {
        return bidCustomRepository.findBidItemIds(username);
    }

    public boolean hasAuthorization(Bid bid, String bidderName) {
        return bid.getBidder().getName().equals(bidderName);
    }

    public boolean isMyItem(Item item, String bidderName) {
        return item.getMember().getName().equals(bidderName);
    }

    private boolean availableBid(Item item) {
        return (item.getItemStatus() != ItemStatus.BID_END) &&
                (item.getItemStatus() != ItemStatus.SOLDOUT) &&
                (itemRedisService.containExpirationQueue(item));
    }
}
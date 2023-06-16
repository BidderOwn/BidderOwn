package site.bidderown.server.bounded_context.bid.service;


import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventItemBidderNotification;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.controller.dto.BidDetails;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidCustomRepository;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.notification.entity.NotificationType;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

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
    private final ApplicationEventPublisher publisher;

    public void create(BidRequest bidRequest, String username) {
        Item item = itemService.getItem(bidRequest.getItemId());
        Member member = memberService.getMember(username);
        Bid bid = Bid.of(bidRequest, member, item);
        bidRepository.save(bid);

        publisher.publishEvent(
                EventItemBidderNotification.of(item, member)
        );
    }

    @Transactional
    public Long handleBid(BidRequest bidRequest, String username) {
        Item item = itemService.getItem(bidRequest.getItemId());
        Member bidder = memberService.getMember(username);
        Optional<Bid> opBid = bidRepository.findByItemAndBidder(item, bidder);

        publisher.publishEvent(EventItemBidderNotification.of(item, bidder));
        publisher.publishEvent(EventSocketConnection.of(bidder.getName(), item.getId(), ConnectionType.ITEM_BIDDER));

        if (opBid.isEmpty()) {
            return create(bidRequest.getItemPrice(), item, bidder);
        }

        Bid bid = opBid.get();
        bid.updatePrice(bidRequest.getItemPrice());
        return bid.getId();
    }

    private Long create(int price, Item item, Member bidder) {
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

    public BidDetails getBidStatistics(Long itemId) {
        Item item = itemService.getItem(itemId);
        return BidDetails.of(item, bidRepository.findMaxPrice(item), bidRepository.findMinPrice(item), bidRepository.findAvgPrice(item));
    }

    public List<Long> getBidItemIds(String username) {
        return bidCustomRepository.findBidItemIds(username);
    }
}
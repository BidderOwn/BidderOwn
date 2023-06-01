package site.bidderown.server.bounded_context.bid.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.controller.dto.BidResponse;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BidService {
    private final BidRepository bidRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    public void resister(BidRequest bidRequest, String username) {
        Item item = itemService.findById(bidRequest.getItemId());
        Member member = memberService.findByUsername(username);
        Bid.of(bidRequest, member, item);
    }

    public List<BidResponse> findList(Long itemId) {
        Item item = itemService.findById(itemId);
        return bidRepository.findByItem(item).stream()
                .map(bid -> BidResponse.of(bid, item)).collect(Collectors.toList());
    }
}

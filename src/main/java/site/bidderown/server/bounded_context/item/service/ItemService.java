package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.base.util.ImageUtils;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemCustomRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCustomRepository itemCustomRepository;
    private final MemberService memberService;
    private final ImageService imageService;
    private final ImageUtils imageUtils;
    private final ApplicationEventPublisher publisher;

    public Item create(ItemRequest request, Long memberId) {
        Member member = memberService.getMember(memberId);
        return _create(request, member);
    }


    public Item create(ItemRequest request, String memberString) {
        Member member = memberService.getMember(memberString);
        return _create(request, member);
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public ItemDetailResponse getItemDetail(Long id) {
        Item item = getItem(id);
        Integer minPrice = itemCustomRepository.minItemPrice(id);
        Integer maxPrice = itemCustomRepository.maxItemPrice(id);
        return ItemDetailResponse.of(item, minPrice, maxPrice);
    }

    public ItemUpdateDto updateById(Long itemId, ItemUpdateDto itemUpdateDto) {
        Item findItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId));

        findItem.update(itemUpdateDto);

        return new ItemUpdateDto(findItem.getTitle(), findItem.getDescription());
    }

    public void delete(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException(itemId));
        itemRepository.delete(findItem);
    }

    private Item _create(ItemRequest request, Member member) {
        Item item = itemRepository.save(Item.of(request, member));
        List<String> fileNames = imageUtils.uploadMulti(request.getImages(), "item");
        imageService.create(item, fileNames);

        publisher.publishEvent(EventSocketConnection.of(
                member.getName(), item.getId(), ConnectionType.ITEM_SELLER));

        return item;
    }

    public List<ItemListResponse> getItems(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findAll(sortCode, searchText, pageable);
    }

    //판매자 아이디 검색
    public List<ItemResponse> getItems(Long memberId) {
        return itemRepository
                .findByMemberId(memberId)
                .stream()
                .map(ItemResponse::of)
                .collect(Collectors.toList());
    }
    public List<ItemResponse> getBidItems(Long memberId) {
        Member member = memberService.getMember(memberId);
        List<Bid> bids = member.getBids();
        return bids.stream().map(bid -> ItemResponse.of(bid.getItem())).collect(Collectors.toList());
    }
}

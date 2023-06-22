package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.base.event.EventSocketDisconnection;
import site.bidderown.server.base.event.EventSoldOutNotification;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
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
        return itemRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다.", id + ""));
    }

    public ItemDetailResponse getItemDetail_V1(Long id) {
        Item item = getItem(id);
        Integer minPrice = itemCustomRepository.minItemPrice(id);
        Integer maxPrice = itemCustomRepository.maxItemPrice(id);
        return ItemDetailResponse.of(item, minPrice, maxPrice);
    }

    public ItemDetailResponse getItemDetail(Long id) {
        return itemCustomRepository.findItemById(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다.", id + ""));
    }

    public ItemUpdate updateById(Long itemId, ItemUpdate itemUpdate) {
        Item findItem = getItem(itemId);
        findItem.update(itemUpdate);
        return new ItemUpdate(findItem.getTitle(), findItem.getDescription());
    }

    @Transactional
    public void updateDeleted(Long itemId) {
        Item item = getItem(itemId);
        item.updateDeleted();
        publisher.publishEvent(EventSocketDisconnection.of(itemId, ConnectionType.ITEM_SELLER));
        publisher.publishEvent(EventSocketDisconnection.of(itemId, ConnectionType.ITEM_BIDDER));
    }

    private Item _create(ItemRequest request, Member member) {
        Item item = itemRepository.save(Item.of(request, member));
        List<String> fileNames = imageUtils.uploadMulti(request.getImages(), "item");
        imageService.create(item, fileNames);

        publisher.publishEvent(EventSocketConnection.of(
                member.getName(), item.getId(), ConnectionType.ITEM_SELLER));

        return item;
    }

    public List<ItemsResponse> getItems_V1(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findItems_v1(sortCode, searchText, pageable);
    }

    public List<ItemsResponse> getItems(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findItems(sortCode, searchText, pageable);
    }

    public List<ItemSimpleResponse> getItems(Long memberId) {
        return itemRepository
                .findByMemberIdAndDeletedIsFalse(memberId)
                .stream()
                .map(ItemSimpleResponse::of)
                .collect(Collectors.toList());
    }

    public List<ItemSimpleResponse> getBidItems(Long memberId) {
        Member member = memberService.getMember(memberId);
        List<Bid> bids = member.getBids();
        return bids.stream()
                .map(Bid::getItem)
                .filter(item -> !item.isDeleted())
                .map(ItemSimpleResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void handleSale(Long itemId, String memberName) {
        Item item = getItem(itemId);

        if (item.getMember().getName().equals(memberName)) {
            throw new ForbiddenException(memberName);
        }

        item.soldOutItem();
        item.getBids().forEach(Bid::updateBidResultFail);
        afterHandleSale(item);
    }

    private void afterHandleSale(Item item) {
        publisher.publishEvent(
                EventSoldOutNotification.of(item)
        );
        publisher.publishEvent(EventSocketDisconnection.of(item.getId(), ConnectionType.ITEM_SELLER));
        publisher.publishEvent(EventSocketDisconnection.of(item.getId(), ConnectionType.ITEM_BIDDER));
    }
}

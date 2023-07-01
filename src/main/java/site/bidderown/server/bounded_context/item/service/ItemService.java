package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    /**
     * @description 성능 테스트를 위해 남겨 둔 메서드입니다. getItemDetail() 사용하시면 됩니다.
     */
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
    public void updateDeleted(Long itemId, String memberName) {
        Item item = getItem(itemId);

        if (!hasAuthorization(item, memberName)) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }

        item.updateDeleted();
    }

    private Item _create(ItemRequest request, Member member) {
        Item item = itemRepository.save(Item.of(request, member));
        List<String> fileNames = imageUtils.uploadMulti(request.getImages(), "item");
        imageService.create(item, fileNames);
        return item;
    }

    /**
     * @description 성능 테스트를 위해 남겨둔 메서드입니다. getItems() 사용하시면 됩니다.
     */
    public List<ItemsResponse> getItems_no_dsl(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findItems_no_dsl(sortCode, searchText, pageable);
    }

    public List<ItemsResponse> getItems_dsl_page(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findItems_dsl_page(sortCode, searchText, pageable);
    }

    public List<ItemsResponse> getItems_dsl_no_offset(Long lastItemId, int sortCode, String searchText, int pageSize) {
        return itemCustomRepository.findItems_dsl_no_Offset(lastItemId, sortCode, searchText, pageSize);
    }

    public List<ItemsResponse> getItems(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findItems(sortCode, searchText, pageable);
    }

    public List<ItemSimpleResponse> getItems(String memberName) {
        Member member = memberService.getMember(memberName);
        return itemRepository
                .findByMemberAndDeletedIsFalse(member)
                .stream()
                .map(ItemSimpleResponse::of)
                .collect(Collectors.toList());
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

    public List<ItemSimpleResponse> getBidItems(String memberName) {
        Member member = memberService.getMember(memberName);
        List<Bid> bids = member.getBids();
        return bids.stream()
                .map(Bid::getItem)
                .filter(item -> !item.isDeleted())
                .map(ItemSimpleResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void soldOut(Long itemId, String memberName) {
        Item item = getItem(itemId);

        if (!hasAuthorization(item, memberName)) {
            throw new ForbiddenException("판매완료 권한이 없습니다.");
        }

        item.soldOutItem();
        item.getBids().forEach(Bid::updateBidResultFail);
    }

    private boolean hasAuthorization(Item item, String memberName) {
        return item.getMember().getName().equals(memberName);
    }
}

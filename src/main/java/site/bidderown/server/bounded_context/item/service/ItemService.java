package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.heart.repository.HeartRepository;
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
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemCustomRepository itemCustomRepository;
    private final ItemRedisService itemRedisService;
    private final MemberService memberService;
    private final ImageService imageService;
    private final HeartRepository heartRepository;


    @Transactional
    public Item create(ItemRequest request, Long memberId) {
        Member member = memberService.getMember(memberId);
        return _create(request, member);
    }

    @Transactional
    public Item create(ItemRequest request, String memberString) {
        Member member = memberService.getMember(memberString);
        return _create(request, member);
    }

    public Item getItem(Long id) {
        return itemRepository.findByIdAndDeletedIsFalse(id)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 상품입니다.", id + ""));
    }

    public ItemDetailResponse getItemDetail(Long id) {
        Item item = itemCustomRepository.findItemById(id);
        return ItemDetailResponse.of(
                item,
                // 상품 입찰 최고가
                itemCustomRepository.findItemBidMaxPriceByItemId(item.getId()),
                // 상품 입찰 최저가
                itemCustomRepository.findItemBidMinPriceByItemId(item.getId()),
                // 상품 count 정보
                itemRedisService.getItemCounts(item)
        );
    }

    /**
     * 성능 테스트를 위한 메서드입니다.
     */
    public ItemDetailResponse getItemDetail__v1(Long id) {
        Item item = itemCustomRepository.findItemById(id);
        return ItemDetailResponse.of__v1(
                item,
                // 상품 입찰 최고가
                itemCustomRepository.findItemBidMaxPriceByItemId(item.getId()),
                // 상품 입찰 최저가
                itemCustomRepository.findItemBidMinPriceByItemId(item.getId()),
                item.getBids().size(),
                item.getComments().size(),
                item.getHearts().size()
        );
    }

    /**
     * @description 테스트를 위한 메서드입니다.
     */
    @Transactional(readOnly = true)
    public List<ItemsResponse> getItems__v1(ItemsRequest itemsRequest, Pageable pageable) {
        List<Item> items = itemCustomRepository.findItems__v1(
                itemsRequest.getS(),
                itemsRequest.getQ(),
                itemsRequest.isFilter(),
                pageable
        );
        return items.stream().map(ItemsResponse::of__v12).toList();
    }

    /**
     * @description 테스트를 위한 메서드입니다.
     */
    @Transactional(readOnly = true)
    public List<ItemsResponse> getItems__v2(ItemsRequest itemsRequest, Pageable pageable) {
        List<Item> items = itemCustomRepository.findItems__v2(
                itemsRequest.getId(),
                itemsRequest.getS(),
                itemsRequest.getQ(),
                itemsRequest.isFilter(),
                pageable
        );
        return items.stream().map(ItemsResponse::of__v12).toList();
    }

    /**
     * Redis 에 item count 정보를 먼저 요청하고 없으면 count 쿼리 생성
     */
    @Transactional(readOnly = true)
    public List<ItemsResponse> getItems(ItemsRequest itemsRequest, Pageable pageable) {
        List<ItemsResponse> items = itemCustomRepository.findItems(
                itemsRequest.getId(),
                itemsRequest.getS(),
                itemsRequest.getQ(),
                itemsRequest.isFilter(),
                pageable
        );
        for (ItemsResponse item : items) {
            item.setCounts(itemRedisService.getItemCounts(item.getId()));
        }
        return items;
    }

    public ItemUpdateResponse getUpdateItem(Long itemId) {
        return ItemUpdateResponse.of(getItem(itemId));
    }

    @Transactional
    public Item updateById(ItemUpdateRequest request, Long itemId, String memberName) {
        Item item = getItem(itemId);

        if (!item.getMember().getName().equals(memberName)) {
            throw new ForbiddenException("수정권한이 없습니다.");
        }

        item.update(request);
        return item;
    }

    @Transactional
    public void updateDeleted(Long itemId, String memberName) {
        Item item = getItem(itemId);

        if (!hasAuthorization(item, memberName)) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }

        item.updateDeleted();
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

    public List<ItemSimpleResponse> getLikeItems(String memberName) {
        Member member = memberService.getMember(memberName);
        List<Heart> hearts = heartRepository.findByMemberId(member.getId());
        return hearts.stream()
                .map(Heart::getItem)
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

    @Transactional
    public void closeBid(Long itemId) {
        Item item = getItem(itemId);
        item.closeBid();
    }

    private Item _create(ItemRequest request, Member member) {
        Item item = itemRepository.save(Item.of(request, member));
        String thumbnailImageFileName = imageService.create(request.getImages(), item);
        item.setThumbnailImageFileName(thumbnailImageFileName);

        return item;
    }

    private boolean hasAuthorization(Item item, String memberName) {
        return item.getMember().getName().equals(memberName);
    }
}

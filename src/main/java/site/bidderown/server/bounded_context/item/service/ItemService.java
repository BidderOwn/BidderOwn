package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.base.util.ImageUtils;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemListResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdateDto;
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
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
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
        return item;
    }
    //전체 리스트
    public Page<ItemListResponse> getAll(Pageable pageable) {
        return itemRepository.findAll(pageable).map(ItemListResponse::of);
    }

    public List<ItemListResponse> getAllQueryDsl(int sortCode, String searchText, Pageable pageable) {
        return itemCustomRepository.findAll(sortCode, searchText, pageable);
    }

    //제목 검색
    public Page<ItemListResponse> searchTitle(String keyword, Pageable pageable) {
        return itemRepository.findByTitleContaining(keyword, pageable).map(ItemListResponse::of);
    }

    //내용 검색
    public Page<ItemListResponse> searchDescription(String keyword, Pageable pageable) {
        return itemRepository.findByDescriptionContaining(keyword, pageable).map(ItemListResponse::of);
    }

    //판매자 아이디 검색
    public List<ItemResponse> getItems(Long memberId) {
        return itemRepository
                .findByMemberId(memberId)
                .stream()
                .map(ItemResponse::of)
                .collect(Collectors.toList());
    }

}

package site.bidderown.server.bounded_context.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.NotFoundException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemResponse;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.repository.MemberRepository;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public ItemResponse create(ItemRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(memberId+""));
        Item item = Item.of(request, member);
        return ItemResponse.from(itemRepository.save(item));
    }

    public List<ItemResponse> findAllByDescription(String description) {
        return itemRepository
                .findAllByDescription(description)
                .stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

    public List<ItemResponse> findAllByTitle(String title, Pageable pageable) {
        return itemRepository
                .findAllByTitleContaining(title, pageable)
                .stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

    public List<ItemResponse> findAllByMemberId(Long memberId) {
        return itemRepository
                .findAllByMemberId(memberId)
                .stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

    public ItemResponse findById(Long itemId) {
        return ItemResponse.from(itemRepository.findById(itemId)
                .orElseThrow(()-> new NotFoundException(itemId + "")));
    }

    public ItemResponse findByItemResponseId(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(itemId + ""));
        return ItemResponse.from(item);
    }

    public List<ItemResponse> findAll() {
        return itemRepository
                .findAll()
                .stream()
                .map(item -> ItemResponse.from(item))
                .collect(Collectors.toList());
    }

    public void delete(Long itemId) {
        Item findItem = itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException(itemId+""));
        itemRepository.delete(findItem);
    }

}

package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.item.controller.dto.*;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemApiController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/list-v1")
    public List<ItemsResponse> getItems__v1(
            @RequestParam(name="s", defaultValue = "1") int sortCode,
            @RequestParam(name = "q", defaultValue = "") String searchText,
            Pageable pageable
    ) {
        return itemService.getItems__v1(sortCode, searchText, pageable);
    }

    @GetMapping("/list")
    public List<ItemsResponse> getItems(
            @RequestParam(name="s", defaultValue = "1") int sortCode,
            @RequestParam(name = "q", defaultValue = "") String searchText,
            @RequestParam(name = "id", required = false) Long lastItemId,
            Pageable pageable
    ) {
        return itemService.getItems(lastItemId, sortCode, searchText, pageable);
    }

    @GetMapping("/list-no-cqrs")
    public List<ItemsResponse> getItems_no_cqrs(
            @RequestParam(name="s", defaultValue = "1") int sortCode,
            @RequestParam(name = "q", defaultValue = "") String searchText,
            @RequestParam(name = "id", required = false) Long lastItemId,
            Pageable pageable
    ) {
        return itemService.getItems_no_cqrs(lastItemId, sortCode, searchText, pageable);
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("isAuthenticated()")
    public String createItem(
            @Valid ItemRequest itemRequest,
            @AuthenticationPrincipal User user
    ) {
        itemService.create(itemRequest, user.getUsername());
        return "/home";
    }

    @GetMapping("/{id}")
    public ItemDetailResponse getDetailItem(@PathVariable Long id) {
        return itemService.getItemDetail(id);
    }

    @GetMapping("/update/{itemId}")
    public ItemUpdateResponse getItemUpdateRequest(@PathVariable Long itemId) {
        return itemService.getUpdateItem(itemId);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{itemId}")
    public String updateItem(
            @PathVariable Long itemId,
            @Valid @RequestBody ItemUpdateRequest itemUpdateRequest,
            @AuthenticationPrincipal User user
    ){
        itemService.updateById(itemUpdateRequest, itemId, user.getUsername());
        return "/item/" + itemId;
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteItem(@PathVariable Long id, @AuthenticationPrincipal User user) {
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(user.getUsername()));
        itemService.updateDeleted(id, user.getUsername());
        return "/home";
    }

    @PutMapping("/sold-out")
    @PreAuthorize("isAuthenticated()")
    public String soldOut(
            @RequestBody ItemSoldOutRequest itemSoldOutRequest,
            @AuthenticationPrincipal User user
    ) {
        itemService.soldOut(itemSoldOutRequest.getItemId(), user.getUsername());
        return "/bid/list?itemId=" + itemSoldOutRequest.getItemId();
    }

    @GetMapping("/me")
    public List<ItemSimpleResponse> getItem(@AuthenticationPrincipal User user) {
        return itemService.getItems(user.getUsername()); //판매상품
    }

    @GetMapping("/bid/me")
    public List<ItemSimpleResponse> getBidItem(@AuthenticationPrincipal User user) {
        return itemService.getBidItems(user.getUsername()); //입찰내역
    }

    @GetMapping("/like/me")
    public List<ItemSimpleResponse> getLikeItem(@AuthenticationPrincipal User user) {
        return itemService.getLikeItems(user.getUsername());
    }
}

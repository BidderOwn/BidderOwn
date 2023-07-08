package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.item.controller.dto.*;
import site.bidderown.server.bounded_context.item.entity.Item;
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

    @GetMapping("/list")
    public List<ItemsResponse> getItems(
            @RequestParam(name="s", defaultValue = "1") int sortCode,
            @RequestParam(name = "q", defaultValue = "") String searchText,
            @RequestParam(name = "id", required = false) Long lastItemId,
            Pageable pageable
    ) {
        return itemService.getItems(lastItemId, sortCode, searchText, pageable);
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

    @GetMapping("/update/get/{id}")
    public ItemUpdateRequest getItemUpdateRequest(@PathVariable Long id) {
        return itemService.createUpdateRequest(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/update/{id}")
    public String ItemUpdateResponse(
            @PathVariable Long id,
            @Valid @RequestBody ItemUpdateResponse itemUpdateResponse,
            @AuthenticationPrincipal User user
    ){
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(id));

        if (!memberDetail.getName().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        itemService.updateById(itemUpdateResponse, id);


        return "/item/" + id;
    }

//    @PostMapping("/updateItem")
//    @PreAuthorize("isAuthenticated()")
//    public ItemUpdate updateItem() {
//
//    }

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

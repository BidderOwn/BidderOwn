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
import site.bidderown.server.base.exception.ForbiddenException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemsResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdate;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;
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
            Pageable pageable
    ) {
        return itemService.getItems(sortCode, searchText, pageable);
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
    public ItemDetailResponse getItem(@PathVariable Long id) {
        return itemService.getItemDetail(id);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ItemUpdate updateItem(
            @PathVariable Long id,
            @RequestBody @Valid ItemUpdate itemUpdate,
            @AuthenticationPrincipal User user
    ){
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(id));

        if (!memberDetail.getName().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        return itemService.updateById(id, itemUpdate);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteItem(@PathVariable Long id, @AuthenticationPrincipal User user) {
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(user.getUsername()));

        if (!memberDetail.getName().equals(user.getUsername())) {
            throw new ForbiddenException("삭제 권한이 없습니다.");
        }

        itemService.updateDeleted(id);
        return "/home";
    }

    @PutMapping("/sale")
    @PreAuthorize("isAuthenticated()")
    public String saleComplete(
            @RequestParam Long itemId,
            @AuthenticationPrincipal User user
    ) {
        itemService.handleSale(itemId, user.getUsername());
        return "/bid/list?itemId=" + itemId;
    }
}

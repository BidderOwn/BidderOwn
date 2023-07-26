package site.bidderown.server.bounded_context.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.item.controller.dto.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 item-api-controller", description = "상품 관련 api 입니다.")
@RequestMapping("/api/v1/item")
public class ItemApiController {

    private final ItemService itemService;
    private final MemberService memberService;
    private final ItemRedisService itemRedisService;

    @GetMapping("/test-increase/{itemId}")
    public void testIncrease(@PathVariable Long itemId) {
        Item item = itemService.getItem(itemId);
        itemRedisService.increaseBidScore(item.getId());
    }

    @GetMapping("/test-decrease/{itemId}")
    public void testDecrease(@PathVariable Long itemId) {
        Item item = itemService.getItem(itemId);
        itemRedisService.decreaseBidScore(item.getId());
    }

    @GetMapping("/test-delete/{itemId}")
    public void testDelete(@PathVariable Long itemId) {
        Item item = itemService.getItem(itemId);
        itemRedisService.removeBidRankingKey(item.getId());
    }

    @Operation(summary = "모든 상품 조회",
               description = """
                       item entity: querydsl, pagination
                       count: jpa 사용
                       min, max: querydsl""")
    @GetMapping("/list-v1")
    public List<ItemsResponse> getItems__v1(ItemsRequest itemsRequest, Pageable pageable) {
        return itemService.getItems__v1(itemsRequest, pageable);
    }

    @Operation(summary = "모든 상품 조회",
               description = """
                       itemsResponse: querydsl, no offset
                       count: querydsl - v1과 유사
                       min, max: querydsl - 정렬 후 limit 1""")
    @GetMapping("/list-v2")
    public List<ItemsResponse> getItems__v2(ItemsRequest itemsRequest, Pageable pageable) {
        return itemService.getItems__v2(itemsRequest, pageable);
    }

    @Operation(summary = "모든 상품 가져오기",
               description = "제목, 가격, 입찰 남은 날짜, 입찰 개수, 댓글 개수를 가져옵니다.")
    @GetMapping("/list")
    public List<ItemsResponse> getItems(ItemsRequest itemsRequest, Pageable pageable) {
        return itemService.getItems(itemsRequest, pageable);
    }

    @Operation(summary = "상품 등록", description = "경매하고 싶은 상품을 등록합니다.")
    @PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
    @PreAuthorize("isAuthenticated()")
    public String createItem(
            @Valid ItemRequest itemRequest,
            @AuthenticationPrincipal User user
    ) {
        itemService.create(itemRequest, user.getUsername());
        return "/home";
    }

    @Operation(summary = "상품 상세정보", description = "id를 이용하여 상품을 조회합니다. - 레디스 적용")
    @GetMapping("/{id}")
    public ItemDetailResponse getDetailItem(@PathVariable Long id) {
        return itemService.getItemDetail(id);
    }

    @Operation(summary = "상품 수정요청", description = "상품 id를 이용하여 사용자가 전에 입력한 데이터 보여줍니다.")
    @GetMapping("/update/{itemId}")
    public ItemUpdateResponse getItemUpdateRequest(@PathVariable Long itemId) {
        return itemService.getUpdateItem(itemId);
    }

    @Operation(summary = "상품 수정", description = "상품 id를 이용하여 수정합니다.")
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

    @Operation(summary = "상품 삭제", description = "상품 작성자가 아이템 삭제 버튼을 눌렀을 경우 상품 id를 이용하여 삭제합니다.")
    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteItem(@PathVariable Long id, @AuthenticationPrincipal User user) {
        itemService.updateDeleted(id, user.getUsername());
        return "/home";
    }

    @Operation(summary = "상품 판매완료", description = "상품 작성자가 판매 완료 버튼을 눌렀을 경우 id를 이용하여 판매완료 처리 합니다.")
    @PutMapping("/sold-out")
    @PreAuthorize("isAuthenticated()")
    public String soldOut(
            @RequestBody ItemSoldOutRequest itemSoldOutRequest,
            @AuthenticationPrincipal User user
    ) {
        itemService.soldOut(itemSoldOutRequest.getItemId(), user.getUsername());
        return "/bid/list?itemId=" + itemSoldOutRequest.getItemId();
    }

    @Operation(summary = "나의 판매 상품", description = "내 정보 페이지에서 나의 판매 상품 리스트를 보여줍니다.")
    @GetMapping("/me")
    public List<ItemSimpleResponse> getItem(@AuthenticationPrincipal User user) {
        return itemService.getItems(user.getUsername());
    }

    @Operation(summary = "내가 입찰한 내역", description = "내 정보 페이지에서 내가 입찰한 내역을 보여즙니다.")
    @GetMapping("/bid/me")
    public List<ItemSimpleResponse> getBidItem(@AuthenticationPrincipal User user) {
        return itemService.getBidItems(user.getUsername());
    }

    @Operation(summary = "관심 표시한 상품", description = "내 정보 페이지에서 내가 관심 표시한 상품을 보여준다.")
    @GetMapping("/like/me")
    public List<ItemSimpleResponse> getLikeItem(@AuthenticationPrincipal User user) {
        return itemService.getLikeItems(user.getUsername());
    }
}

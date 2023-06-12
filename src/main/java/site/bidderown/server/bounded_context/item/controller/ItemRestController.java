package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemListResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdateDto;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemRestController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ItemDetailResponse getItem(@PathVariable Long id) {
        return ItemDetailResponse.of(itemService.getItem(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    public ItemUpdateDto updateItem(@PathVariable Long id, @RequestBody @Valid ItemUpdateDto itemUpdateDto, Principal principal){
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(id));

        if (!memberDetail.getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        return itemService.updateById(id, itemUpdateDto);
    }

    //제목, 내용 검색 (판매자ID는 추가 할 예정)
    @GetMapping("/list/search")
    public Page<ItemListResponse> searchTitle(String type, String keyword, Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemListResponse> itemList = null;

        if(keyword == null) {
            itemList = itemService.getAll(pageable);
        } else {
            switch (type) {
                case "title" :
                    itemList = itemService.searchTitle(keyword, pageable);
                    break;
                case "description" :
                    itemList = itemService.searchDescription(keyword, pageable);
                    break;
            }
        }

        model.addAttribute("itemList", itemList);

        return itemList;
    }

    @GetMapping("/list")
    public Page<ItemListResponse> showList(
            @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return itemService.getAll(pageable);
    }
}

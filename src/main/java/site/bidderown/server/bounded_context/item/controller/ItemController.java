package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.item.controller.dto.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    @ResponseBody
    public ItemDetailResponse getItem(@PathVariable Long id) {
        return ItemDetailResponse.of(itemService.getItem(id));
    }

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ItemResponse createItem(
            @Valid ItemRequest itemRequest,
            @AuthenticationPrincipal User user
    ) {
        return ItemResponse.of(itemService.create(itemRequest, user.getUsername()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/update/{id}")
    @ResponseBody
    public ItemUpdateDto updateItem(@PathVariable Long id, @RequestBody @Valid ItemUpdateDto itemUpdateDto, Principal principal){
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(id));

        if (!memberDetail.getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }

        return itemService.updateById(id, itemUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteItem(@PathVariable Long id, Principal principal) {
        MemberDetail memberDetail = MemberDetail.of(memberService.getMember(id));

        if (!memberDetail.getName().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }
        itemService.delete(id);
        return "redirect:/item/list";
    }

    //제목, 내용 검색 (판매자ID는 추가 할 예정)
    @GetMapping("/list/search")
    @ResponseBody
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
    @ResponseBody
    public Page<ItemListResponse> showList(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ItemListResponse> itemList = itemService.getAll(pageable);
        model.addAttribute("itemList", itemList);
        return itemList;
    }

}

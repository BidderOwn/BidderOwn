package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/item")
public class ItemApiController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping("/{id}")
    public ItemDetailResponse getItem(@PathVariable Long id) {
        return itemService.getItemDetail(id);
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

    @GetMapping("/list")
    public List<ItemListResponse> showList(
            @RequestParam(name="s", defaultValue = "1") int sortCode,
            @RequestParam(name = "q", defaultValue = "") String searchText,
            Pageable pageable
    ) {
        return itemService.getItems(sortCode, searchText, pageable);
    }
}

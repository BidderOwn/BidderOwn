package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemResponse;
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
    public ItemResponse getItem(@PathVariable Long id) {
        return ItemResponse.of(itemService.getItem(id));
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ItemResponse> getItemList() {
        return itemService.getAll();
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ItemResponse createItem(
            @Valid ItemRequest itemRequest,
            @AuthenticationPrincipal User user) {
        return ItemResponse.of(itemService.create(itemRequest, user.getUsername()));
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

}

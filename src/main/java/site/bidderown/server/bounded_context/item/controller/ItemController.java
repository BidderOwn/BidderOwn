package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import site.bidderown.server.bounded_context.member.entity.Member;
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

    @GetMapping("/login")
    public String testLogin() {
        return "/usr/login";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user) {
        log.info(user.getUsername());
        return "/usr/item/home";
    }

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


    @PostMapping("/")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public ItemResponse createItem(@RequestBody @Valid ItemRequest itemRequest, Member member) {
        //member를 Authentication에서 받아오는 것으로 수정
        return ItemResponse.of(itemService.create(itemRequest, member.getId()));
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

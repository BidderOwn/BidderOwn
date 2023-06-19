package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.service.MemberService;

import javax.validation.Valid;
import java.security.Principal;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;
    private final MemberService memberService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String showCreateItem() {
        return "/usr/item/create";
    }

    @GetMapping("/{id}")
    public String showItemDetail(Model model,  @PathVariable Long id) {
        model.addAttribute("item", itemService.getItemDetail(id));
        return "/usr/item/detail";
    }

    @GetMapping("/sale")
    @PreAuthorize("isAuthenticated()")
    public String saleComplete(
            @RequestParam("itemId") Long itemId,
            @AuthenticationPrincipal User user
    ) {
        itemService.handleSale(itemId);
        return "redirect:/bid/list?itemId=" + itemId;
    }
}

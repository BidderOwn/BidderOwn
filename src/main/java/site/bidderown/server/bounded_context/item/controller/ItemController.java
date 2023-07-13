package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public String showCreateItem() {
        return "usr/item/create";
    }

    @GetMapping("/{id}")
    public String showItemDetail(Model model,  @PathVariable Long id) {
        model.addAttribute("itemId", id);
        return "usr/item/detail";
    }
    @GetMapping("/sale-list")
    public String showSaleItemList() {
        return "usr/item/sale_list";
    }

    @GetMapping("/heart-list")
    public String showHeartItemList() {
        return "usr/item/heart_list";
    }

    @GetMapping("/bid-list")
    public String showBidItemList() {
        return "usr/item/bid_list";
    }
    @GetMapping("/update/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showUpdateItem(Model model, @PathVariable Long id) {
        model.addAttribute("itemId", id);
        return "/usr/item/update";
    }
}

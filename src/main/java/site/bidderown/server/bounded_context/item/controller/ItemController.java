package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemResponse;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

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
        return itemService.findById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<ItemResponse> getItemList() {
        List<ItemResponse> itemList = itemService.findAll();
        return itemList;
    }


    @PostMapping("/create")
    @ResponseBody
    public ItemResponse createItem(@RequestBody @Valid ItemRequest itemRequest, Member member) {
        //member를 Authentication에서 받아오는 것으로 수정
        return itemService.create(itemRequest, member.getId());
    }

    // 권한 체크 나중에
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {
        itemService.delete(id);
        return "redirect:/item/list";
    }

}

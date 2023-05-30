package site.bidderown.server.bounded_context.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
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
    public Optional<Item> getItem(@PathVariable Long id) {
        return itemService.findById(id);
    }

    @GetMapping("/list")
    @ResponseBody
    public List<Item> getItemList() {
        List<Item> itemList = itemService.findAll();
        return itemList;
    }

    //유저 계정 등록 해줘야함
    @PostMapping("/create")
    @ResponseBody
    public Item createItem(@RequestBody @Valid Item item) {
        return itemService.create(item);
    }

    // 권한 체크 나중에
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {
        Item item = itemService.findById(id).orElse(null);
        itemService.delete(item);
        return "redirect:/items/list";
    }
}

package site.bidderown.server.bounded_context.item.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class ItemController {
    @GetMapping("/login")
    public String testLogin() {
        return "/usr/login";
    }

    @GetMapping("/")
    public String home(@AuthenticationPrincipal User user) {
        log.info(user.getUsername());
        return "/usr/item/home";
    }
}

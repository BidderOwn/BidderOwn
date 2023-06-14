package site.bidderown.server.bounded_context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.bounded_context.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final ApplicationEventPublisher publisher;

    @GetMapping("/login")
    public String testLogin() {
        return "/usr/login";
    }

    @GetMapping("/")
    public String redirectHome(){
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(
            Model model,
            @AuthenticationPrincipal User user,
            @RequestParam(name = "q", defaultValue = "") String searchText
    ) {
            if (user != null) {
                model.addAttribute("username", user.getUsername());
            }
        model.addAttribute("searchText", searchText);
        return "/usr/item/home";
    }
}

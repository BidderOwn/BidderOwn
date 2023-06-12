package site.bidderown.server.bounded_context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String testLogin() {
        return "/usr/login";
    }

    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        return "/usr/item/home";
    }
}

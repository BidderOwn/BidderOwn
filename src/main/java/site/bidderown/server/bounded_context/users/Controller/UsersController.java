package site.bidderown.server.bounded_context.users.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UsersController {
    // TODO 로그인 페이지 변경
    @GetMapping("/login-test")
    public String testLogin() {
        return "/usr/login";
    }

    @GetMapping("/")
    public String home() {
        return "/usr/home";
    }
}

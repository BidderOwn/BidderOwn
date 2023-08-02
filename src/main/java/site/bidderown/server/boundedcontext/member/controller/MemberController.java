package site.bidderown.server.boundedcontext.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.boundedcontext.member.controller.dto.MemberDetail;
import site.bidderown.server.boundedcontext.member.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Value("${custom.socket.path}")
    private String socketPath;

    @GetMapping("/my-page")
    @PreAuthorize("isAuthenticated()")
    public String myPage(Model model, @AuthenticationPrincipal User user) {
        MemberDetail member = MemberDetail.from(memberService.getMember(user.getUsername()));
        model.addAttribute("nickname", member.getName());
        return "usr/my_page";
    }

    @GetMapping("/form-login")
    public String loginPage(Model model) {
        return "usr/form_login";
    }


    @Operation(summary = "소켓 정보 조회", description = "자신의 소켓 정보를 조회합니다.")
    @GetMapping("/api/v1/socket-id")
    @ResponseBody
    public String getSocketId(@AuthenticationPrincipal User user) {
        if(user == null)
            throw new ForbiddenException("로그인 후 접근이 가능합니다.");
        return socketPath + memberService.getMember(user.getUsername()).getId();
    }
}
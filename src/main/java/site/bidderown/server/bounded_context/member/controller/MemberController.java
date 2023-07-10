package site.bidderown.server.bounded_context.member.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.bounded_context.item.controller.dto.ItemSimpleResponse;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.controller.dto.MemberDetail;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ItemService itemService;

    @Value("${custom.socket.path}")
    private String socketPath;

    @GetMapping("/my-page")
    @PreAuthorize("isAuthenticated()")
    public String myPage(Model model, @AuthenticationPrincipal User user) {
        Member member = memberService.getMember(user.getUsername());
        MemberDetail memberDetail = MemberDetail.of(member);

        model.addAttribute("nickname", memberDetail.getName());

        List<ItemSimpleResponse> items = itemService.getItems(member.getId());
        List<ItemSimpleResponse> bidItems = itemService.getBidItems(member.getId());

        model.addAttribute("items", items);
        model.addAttribute("bidItems",bidItems);

        return "usr/my_page";
    }

    @GetMapping("/form-login")
    public String loginPage(Model model) {
        return "usr/form_login";
    }

    @GetMapping("/api/v1/socket-id")
    @ResponseBody
    public String getSocketId(@AuthenticationPrincipal User user) {
        if(user == null)
            throw new ForbiddenException("로그인 후 접근이 가능합니다.");
        return socketPath + memberService.getMember(user.getUsername()).getId();
    }
}
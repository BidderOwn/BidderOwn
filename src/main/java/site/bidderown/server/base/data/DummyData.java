package site.bidderown.server.base.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.boundedcontext.member.service.MemberService;

@RequiredArgsConstructor
@Component
public class DummyData {

    private final MemberService memberService;

    @Transactional
    public void init() {
        initMembers();
    }

    private void initMembers() {
        memberService.join("user" + 1, "1234");
        memberService.join("user" + 2, "1234");
    }
}

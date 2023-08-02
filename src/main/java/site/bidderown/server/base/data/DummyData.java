package site.bidderown.server.base.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

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

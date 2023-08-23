package site.bidderown.server.base.data;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.repository.ItemRepository;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class DummyData {

    private final MemberService memberService;
    private final ItemRepository itemRepository;

    @Transactional
    public void init() {
        initMembers();
    }

    private void initMembers() {
        Member member1 = memberService.join("user1", "1234");
        memberService.join("user2", "1234");

        for (int i = 0; i < 30; i++) {
            itemRepository.save(
                    Item.builder()
                            .title("test")
                            .description("desc")
                            .expireAt(LocalDateTime.of(2023, 12, 31, 0, 0))
                            .member(member1)
                            .minimumPrice(1000)
                            .build()
            );
        }
    }
}

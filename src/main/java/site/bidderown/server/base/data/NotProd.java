package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.heart.repository.HeartRepository;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.ArrayList;
import java.util.List;

@Profile({"dev"})
@Configuration
@Transactional
public class NotProd {
    private boolean initDataDone = true;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ItemRepository itemRepository,
            BidRepository bidRepository,
            CommentRepository commentRepository,
            HeartRepository heartRepository
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;

            Member member1 = memberService.join("user1", "1234");
            Member member2 = memberService.join("user2", "1234");
            Member member3 = memberService.join("모찌맘", "1234");
            Member member4 = memberService.join("다생이", "1234");
            Member member5 = memberService.join("꿀벌", "1234");
            Member member6 = memberService.join("apel1443", "1234");
            Member member7 = memberService.join("태훈", "1234");
            Member member8 = memberService.join("shfmdnpdl", "1234");
            Member member9 = memberService.join("노트", "1234");
            Member member10 = memberService.join("woozu", "1234");
            Member member11 = memberService.join("network", "1234");

            Member kakaoMember1 = memberService.loginAsSocial("KAKAO_2810203532");
            Member kakaoMember2 = memberService.loginAsSocial("KAKAO_2829157954");
            Member kakaoMember3 = memberService.loginAsSocial("KAKAO_2829504082");

            List<Member> members = List.of(member1, member2, member3, member4, member5, member6, member7, member8, member9, member10, member11);

            // 아이템 등록
            ArrayList<Item> itemList = new ArrayList<>();
            ArrayList<Bid> bidList = new ArrayList<>();
            ArrayList<Comment> commentList = new ArrayList<>();
            ArrayList<Heart> heartList = new ArrayList<>();

            for (int i = 0; i < 100_000; i++) {
                Item item = Item.of(ItemRequest.builder().
                                title("not prod")
                                .description("not prod")
                                .period(3)
                                .minimumPrice(1300)
                                .build(),
                        members.get((int) ((Math.random() * 10000) % 10))
                );
                itemList.add(item);
                for (int j = 0; j < 10; j++) {
                    bidList.add(
                            Bid.of(10000, members.get((int) ((Math.random() * 10000) % 10)), item)
                    );
                    commentList.add(
                            Comment.of(CommentRequest.of("not-prod-test"), item, members.get((int) ((Math.random() * 10000) % 10)))
                    );
                    heartList.add(
                            Heart.builder()
                                    .item(item)
                                    .member(members.get((int) ((Math.random() * 10000) % 10)))
                                    .build()
                    );
                }
            }
            itemRepository.saveAll(itemList);
            bidRepository.saveAll(bidList);
            commentRepository.saveAll(commentList);
            heartRepository.saveAll(heartList);
        };
    }
}

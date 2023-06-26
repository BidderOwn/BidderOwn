package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import site.bidderown.server.bounded_context.bid.controller.dto.BidRequest;
import site.bidderown.server.bounded_context.bid.repository.BidJdbcRepository;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemJdbcRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@Profile({"dev"})
@Configuration
public class NotProd {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ItemRepository itemRepository,
            CommentService commentService,
            ImageService imageService,
            BidService bidService,
            ItemJdbcRepository itemJdbcRepository,
            BidJdbcRepository bidJdbcRepository
    ) {
        return args -> {

            if (initDataDone) return;

            initDataDone = true;
            // 유저 생성
//            IntStream.rangeClosed(1, 10)
//                    .forEach(i -> memberService.loginAsSocial("user_" + i));

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
            List<Member> members = List.of(member1, member2, member3, member4, member5, member6, member7, member8, member9, member10, member11);


            Member kakaoMember1 = memberService.loginAsSocial("KAKAO_2810203532");
            Member kakaoMember2 = memberService.loginAsSocial("KAKAO_2829157954");
            Member kakaoMember3 = memberService.loginAsSocial("KAKAO_2829504082");

            long startTime = System.currentTimeMillis();

            List<Item> items = List.of(
                    Item.of(ItemRequest.builder().
                            title("삼성 제습기")
                            .description("15리터 실사용 10회정도 작동 잘 돼요\n 소리도 적어요 계산동 직거래 오후6시 이후")
                            .period(3)
                            .minimumPrice(130_000)
                            .build(), member1),

                    Item.of(ItemRequest.builder().
                            title("캐리어 여행용캐리어 28인치캐리어")
                            .description(
                                    """
                                            28인치 여행용캐리어입니다
                                            확장형이며 tsa자물쇠이며
                                            pc90프로+abs10프로미만 제품입니다
                                            실버 7만원
                                            네이비 7만원
                                            로즈골드 7만원과
                                            로즈골드 8만원 있습니다
                                            이제품은 매번 나오지않습니다"""
                            ).period(4)
                            .minimumPrice(70_000)
                            .build(), member2),

                    Item.of(ItemRequest.builder().
                            title("폴로 반팔 니트 - 스몰사이즈")
                            .description("""
                            어깨:49 가슴:59 소매:66 총장:72
                                                        
                            신형 폴로 워셔블 니트 입니다
                                                        
                            국내 남성 105 사이즈 입니다
                                                        
                            네고 및 교환 문의는 정중히 사양 하겠습니다
                            """)
                            .period(4)
                            .minimumPrice(68_000)
                            .build(), member3),
                    Item.of(ItemRequest.builder().
                            title("신발처분")
                            .description("""
                                    아식스 운동화로 1 ***-****-**** 제품입니다
                                    사이즈가 커서 밑바닥부분 보시면아시겠지만 몇번밖에 신지안아 상태도 많이좋습니다.
                                    사이즈 235이고 보시고 구매하셔도됩니다
                                    """)
                            .period(1)
                            .minimumPrice(75_000)
                            .build(), member4),
                    Item.of(ItemRequest.builder().
                            title("정품 풀박스 샤넬백 샤넬가방")
                            .description("""
                                    샤넬에서 VIP 생일선물로 주신 풀박스 정품이구요
                                    실사보시면 아시듯
                                    진짜 영롱보스 너무 예쁘죠 ✨
                                    """)
                            .period(5)
                            .minimumPrice(450_000)
                            .build(), member5),
                    Item.of(ItemRequest.builder().
                            title("삼성 전자레인지")
                            .description("""
                                    삼성 전자렌지 가져가세요 작동 잘됩니다.
                                                                        
                                    정면이 블랙으로 되있는데
                                    너무 적나라하게 비쳐서 비스듬히 찍었어요;
                                                                        
                                    전선때문에 위쪽 이염 있는데 성능에 아무 문제없어요~
                                                                        
                                    사이즈 가로49 세로 32.5 높이 26cm
                                    """)
                            .period(4)
                            .minimumPrice(30_000)
                            .build(), member6),
                    Item.of(ItemRequest.builder().
                            title("바이로와일드s 급처 속도")
                            .description("""
                                    배터리 추가옵션으로 정품 대용량 구매
                                                                        
                                    구매하고 몇번 못탔는데 이사가면서 정리합니다. 배달용으로 안쓰고 저녁에 해안로 다니는 마실용으로만 썼어요.
                                                                        
                                    관리하면서 타서 크게 보이는 기스는없고 생활기스정도는 있을거라고 생각 됩니다. 사진에 노란색으로 표시한부분 머드가드 살짝 나갔습니다. 구매당시 받았던 사은품 핼멧.사이드미러2개 미사용 같이 드립니다.
                                                                        
                                    스로틀만 땡겨도 45km 까지 나갑니다.
                                    """)
                            .period(4)
                            .minimumPrice(1400000)
                            .build(), member7),
                    Item.of(ItemRequest.builder().
                            title("아이폰14프로")
                            .description("아이폰14프로 아이폰13프로맥스 아이폰13프로 아이폰13 아이폰12 아이폰11프로 아이폰11 S급~B급 팝니다")
                            .period(5)
                            .minimumPrice(500_000)
                            .build(), member8),
                    Item.of(ItemRequest.builder().
                            title("티파니앤코 목걸이")
                            .description("""
                                    현대백화점 판교점에서 구매 한 티파니앤코 정품 목걸이입니다.
                                    아무리 찾아도 보증서가 안보여서 저렴하게 정리해요
                                    사진속 구성품은 모두 있습니다.
                                    문의는 챗 주세요 ㅎㅎ
                                    """)
                            .period(5)
                            .minimumPrice(275_000)
                            .build(), member9),
                    Item.of(ItemRequest.builder().
                            title("applewatch 애플워치")
                            .description("""
                            applewatch 애플워치 스트랩 44mm 가죽밴드
                            판매합니다.
                                                        
                            진짜가죽은 아니고 합성피혁같긴해요.
                                                        
                            거래관련사항) 무례하지 않으신 분,
                            중고물품의 특성을 잘 아시는분과 거래원합니다.
                            """)
                            .period(3)
                            .minimumPrice(140_000)
                            .build(), member10),
                    Item.of(ItemRequest.builder().
                            title("보테가베네타 쇼퍼백 팔아요")
                            .description("정품인데 사용감도 많고 관리도 잘 안됏고 보증서도 없어서 싸게 팔아요")
                            .period(5)
                            .minimumPrice(30000)
                            .build(), member11)

            );

            itemRepository.saveAll(items);

            bidService.create(BidRequest.of(items.get(0).getId(), 145_000), members.get(1).getName());
            bidService.create(BidRequest.of(items.get(0).getId(), 120_000), members.get(2).getName());

            bidService.create(BidRequest.of(items.get(1).getId(), 90_000), members.get(0).getName());
            bidService.create(BidRequest.of(items.get(1).getId(), 75_000), members.get(2).getName());
            bidService.create(BidRequest.of(items.get(1).getId(), 80_000), members.get(3).getName());

            bidService.create(BidRequest.of(items.get(2).getId(), 70_000), members.get(3).getName());
            bidService.create(BidRequest.of(items.get(2).getId(), 65_000), members.get(4).getName());

            bidService.create(BidRequest.of(items.get(3).getId(), 76_000), members.get(2).getName());
            bidService.create(BidRequest.of(items.get(3).getId(), 70_000), members.get(1).getName());

            bidService.create(BidRequest.of(items.get(4).getId(), 460_000), members.get(9).getName());
            bidService.create(BidRequest.of(items.get(4).getId(), 430_000), members.get(8).getName());

            bidService.create(BidRequest.of(items.get(5).getId(), 30_000), members.get(1).getName());
            bidService.create(BidRequest.of(items.get(5).getId(), 27_000), members.get(2).getName());
            bidService.create(BidRequest.of(items.get(5).getId(), 28_000), members.get(3).getName());
            bidService.create(BidRequest.of(items.get(5).getId(), 27_000), members.get(0).getName());

            bidService.create(BidRequest.of(items.get(6).getId(), 1450000), members.get(3).getName());
            bidService.create(BidRequest.of(items.get(6).getId(), 1430000), members.get(4).getName());

            bidService.create(BidRequest.of(items.get(7).getId(), 505_000), members.get(6).getName());
            bidService.create(BidRequest.of(items.get(7).getId(), 480_000), members.get(8).getName());

            bidService.create(BidRequest.of(items.get(8).getId(), 280_000), members.get(1).getName());
            bidService.create(BidRequest.of(items.get(8).getId(), 270_000), members.get(2).getName());
            bidService.create(BidRequest.of(items.get(8).getId(), 290_000), members.get(3).getName());

            bidService.create(BidRequest.of(items.get(9).getId(), 143_000), members.get(4).getName());
            bidService.create(BidRequest.of(items.get(9).getId(), 147_000), members.get(5).getName());
            bidService.create(BidRequest.of(items.get(9).getId(), 142_000), members.get(3).getName());
            bidService.create(BidRequest.of(items.get(9).getId(), 141_000), members.get(2).getName());
            bidService.create(BidRequest.of(items.get(9).getId(), 139_000), members.get(1).getName());

            for (int i = 0; i < 11; i++) {
                imageService.create(items.get(i), List.of("image" + (i + 1) + ".jpeg"));

                if (i == 10) {
                    commentService.create(CommentRequest.of("어디서 거래 가능하세요?"), items.get(i).getId(), members.get(0));
                } else {
                    commentService.create(CommentRequest.of("어디서 거래 가능하세요?"), items.get(i).getId(), members.get(i + 1));
                }
            }
        };
    }
}
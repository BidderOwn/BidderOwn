package site.bidderown.server.base.data;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.bounded_context.bid.repository.BidJdbcRepository;
import site.bidderown.server.bounded_context.bid.repository.BidRepository;
import site.bidderown.server.bounded_context.bid.service.BidService;
import site.bidderown.server.bounded_context.comment.repository.CommentRepository;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemJdbcRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRedisRepository;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemRedisService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.List;

@Profile({"leuiprod", "dev"})
@Configuration
@Transactional
public class NotProd {
    private boolean initDataDone = false;

    @Bean
    CommandLineRunner initData(
            MemberService memberService,
            ItemRepository itemRepository,
            CommentService commentService,
            ImageService imageService,
            BidService bidService,
            BidRepository bidRepository,
            CommentRepository commentRepository,
            ItemRedisRepository itemRedisRepository,
            ItemJdbcRepository itemJdbcRepository,
            BidJdbcRepository bidJdbcRepository,
            ItemRedisService itemRedisService
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
                            title("나이키 에어포스 1 '07 로우 화이트")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(111000)
                            .build(), member1),

                    Item.of(ItemRequest.builder().
                            title("애플 에어팟 맥스 실버")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(130000)
                            .build(), member2),
                    Item.of(ItemRequest.builder().
                            title("팔라스 x WWE 나이스 데이 티셔츠 화이트 - 23SS")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(160000)
                            .build(), member3),

                    Item.of(ItemRequest.builder().
                            title("나이키 터미네이터 하이 그래닛 앤 다크 옵시디언")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(110000)
                            .build(), member4),
                    Item.of(ItemRequest.builder().
                            title("보테가 베네타 미니 레더 카세트백 화이트")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(1167000)
                            .build(), member5),

                    Item.of(ItemRequest.builder().
                            title("플레이 꼼데가르송 레드 하트 티셔츠 블랙")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(80000)
                            .build(), member6),
                    Item.of(ItemRequest.builder().
                            title("스타벅스 x 웜그레이테일 23 서머 허기베어 키체인")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(30000)
                            .build(), member7),

                    Item.of(ItemRequest.builder().
                            title("팔라스 더블 집 자켓 스모크 블루 - 23SS")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(230000)
                            .build(), member8),
                    Item.of(ItemRequest.builder().
                            title("슈프림 x 팀버랜드 리플렉티브 테이핑 아노락 스톤 - 21SS")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(425000)
                            .build(), member9),

                    Item.of(ItemRequest.builder().
                            title("스와치 x 오메가 바이오세라믹 문스와치 미션 투 우라노스")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(419000)
                            .build(), member11),
                    Item.of(ItemRequest.builder().
                            title("카시오 지샥 x 디스이즈네버댓 DW-5600TINT23-7DF 스켈레톤 (스페셜 패키지 화이트 버전)")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(230000)
                            .build(), member5),

                    Item.of(ItemRequest.builder().
                            title("샤넬 클래식 카드 홀더 그레인드 카프스킨 & 실버 메탈 블랙")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(1130000)
                            .build(), member8),
                    Item.of(ItemRequest.builder().
                            title("아이앱 스튜디오 우븐 쇼츠 네이비- 더현대 서울 한정")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(130000)
                            .build(), member7),

                    Item.of(ItemRequest.builder().
                            title("휴먼 메이드 그래픽 티셔츠 #13 화이트")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(160000)
                            .build(), member9),
                    Item.of(ItemRequest.builder().
                            title("(W) 헌터 오리지널 쇼트 레인 부츠 블랙")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(230000)
                            .build(), member4),

                    Item.of(ItemRequest.builder().
                            title("샤넬 코드 컬러 리미티드 에디션 미르와르 두블르 화세뜨 111 발레리나")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(130000)
                            .build(), member3),
                    Item.of(ItemRequest.builder().
                            title("몽클레르 로고 베이스볼 캡 블랙 - 23SS")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(130000)
                            .build(), member5),

                    Item.of(ItemRequest.builder().
                            title("루이비통 LV 트레이너 스니커즈 모노그램 데님 블랙")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(1500000)
                            .build(), member6),
                    Item.of(ItemRequest.builder().
                            title("(W) 샤넬 스니커즈 카프스킨 & 화이트")
                            .description("1년 반정도 전에 선물 받았구요\n" +
                                    "집에 따로 있어서 별로 사용하지 않았습니다\n" +
                                    "특히 작년 6월부터 오늘 까지 아예 보관만 하였습니다" +
                                    " 홍대입구 직거래 오후6시 이후 ")
                            .period(3)
                            .minimumPrice(1800000)
                            .build(), member1),

                    Item.of(ItemRequest.builder().
                            title("(W) 발렌시아가 스트라이크 1917 오버사이즈 후드 오프 화이트")
                            .description("미개봉입니다\n" +
                                    "\n" +
                                    "사진 3장 참고해 주세요\n" +
                                    "\n" +
                                    "e편한세상금빛그랑메종 4단지 직거래\n" +
                                    "택배비별도")
                            .period(3)
                            .minimumPrice(450000)
                            .build(), member11)

            );
            itemRepository.saveAll(items);

            for (int i = 0; i < 20; i++) {
                imageService.create(items.get(i), List.of("image" + (i + 1) + ".webp"));
                items.get(i).setThumbnailImageFileName("image" + (i + 1) + ".webp");
                itemRepository.save(items.get(i));
                itemRedisRepository.save(items.get(i).getId(), 3);
            }



        };
    }
}

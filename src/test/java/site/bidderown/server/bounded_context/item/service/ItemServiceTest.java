package site.bidderown.server.bounded_context.item.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.resolver.PathResolver;
import site.bidderown.server.base.util.ImageUtils;
import site.bidderown.server.bounded_context.image.service.ImageService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemDetailResponse;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemsResponse;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemService itemService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ImageUtils imageUtils;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PathResolver pathResolver;

    private final int PAGE_SIZE = 10;
    private final int ITEM_SIZE = 5;

    @BeforeEach
    void beforeEach() throws IOException {
        Member member = memberService.join("test_member_1", "");
        initItemData(member);
    }

    @AfterEach
    void afterEach() {
        deleteTestImage();
    }

    @Test
    @DisplayName("상품 등록")
    void test001() {
        List<Item> item = itemRepository.findByTitle("test_title_0");

        assertEquals("test_title_0", item.get(0).getTitle());

    }

    @DisplayName("상품 전체 조회 테스트 - 최신순 sortCode 1")
    @Test
    void test002() {
        //given
        int sortCode = 1;
        String searchText = "test";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(sortCode, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items).isSortedAccordingTo(
                Comparator.comparing(ItemsResponse::getId, Comparator.reverseOrder())
        );
    }

    @DisplayName("상품 정렬 테스트 - 인기순 sortCode 2")
    @Test
    void test003() {
        //given
        int sortCode = 2;
        String searchText = "test";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(sortCode, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items).isSortedAccordingTo(
                Comparator.comparing(ItemsResponse::getBidCount, Comparator.reverseOrder())
        );
    }

    @DisplayName("상품 정렬 테스트 - 경매종료 마감순 sortCode 2")
    @Test
    void test004() {
        //given
        int sortCode = 3;
        String searchText = "test";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(sortCode, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(5);
        assertThat(items).isSortedAccordingTo(
                Comparator.comparing(ItemsResponse::getExpireAt)
        );
    }

    @DisplayName("상품 검색 테스트 - 검색어 제목 'test_title_1'")
    @Test
    void test005() {
        //given
        String searchText = "test_title_1";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(0, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(1);

        ItemsResponse itemResponse = items.get(0);
        assertThat(itemResponse.getTitle().contains(searchText)).isTrue();
    }

    @DisplayName("상품 검색 테스트 - 검색어 내용 'test_description_1'")
    @Test
    public void test006() {
        //given
        String searchText = "test_description_1";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(0, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(1);

        ItemsResponse itemResponse = items.get(0);
        Item item = itemService.getItem(itemResponse.getId());

        assertThat(item.getDescription().contains(searchText)).isTrue();
    }

    @DisplayName("상품 검색 테스트 - 검색어(작성자) 'test_member_1'")
    @Test
    void test007() {
        //given
        String searchText = "test_member_1";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(0, searchText, pageRequest);

        //then
        assertThat(items.size()).isEqualTo(5);
        items.stream()
                .map(item -> itemService.getItem(item.getId()))
                .forEach(item -> assertThat(item.getMember().getName().contains(searchText)).isTrue());
    }

    @DisplayName("상품 정렬, 검색 테스트 - 인기순, 검색어 'test_'")
    @Test
    void test008() {
        //given
        String searchText = "test_";
        PageRequest pageRequest = PageRequest.of(0, PAGE_SIZE);

        //when
        List<ItemsResponse> items = itemService.getItems(0, searchText, pageRequest);

        //then
        assertThat(items).isSortedAccordingTo(
                Comparator.comparing(ItemsResponse::getBidCount, Comparator.reverseOrder())
        );
        assertThat(items.size()).isEqualTo(5);
    }


    @Test
    @DisplayName("단일 상품 조회")
    void test009() {
        List<Item> item = itemRepository.findByTitle("test_title_0");
        ItemDetailResponse itemDetailResponse= ItemDetailResponse.of(item.get(0),1000,10000);

        ItemDetailResponse itemDetail = itemService.getItemDetail(itemDetailResponse.getId());

        assertEquals("test_title_0", itemDetail.getTitle());


    }

    @Test
    @DisplayName("상품 삭제(권한 검사까지)")
    void test010() {
        Member member = memberService.getMember("test_member_1");
        List<Item> item = itemRepository.findByTitle("test_title_1");
        itemService.updateDeleted(item.get(0).getId(),member.getName()); //상품 상태가 true로 바뀜
        assertEquals(item.get(0).isDeleted(), true);
    }

    /**
     * @description 테스트 아이템 초기화
     * @param member
     * @throws IOException
     */
    private void initItemData(Member member) throws IOException {
        String testTitlePrefix = "test_title_";
        String testDescriptionPrefix = "test_description_";
        for (int i = 0; i < ITEM_SIZE; i++) {
            createTestItem(
                    new ItemRequest(
                            testTitlePrefix + i,
                            1000,
                            i + 1,
                            testDescriptionPrefix + i,
                            List.of(generateMockImageFile())
                    ), member
            );
        }
    }

    /**
     * @description 테스트용 이미지 생성
     * @return MockMultipartFile
     * @throws IOException
     */
    private MockMultipartFile generateMockImageFile() throws IOException {
        return new MockMultipartFile(
                "image",
                "test_image.png",
                "image/png",
                new ClassPathResource("/static/image/test_image.png").getInputStream()
        );
    }

    /**
     * @description 테스트 아이템 생성 함수
     * item 이미지 파일 저장 경로: '/resources/images/test'
     */
    private Item createTestItem(ItemRequest request, Member member) {
        Item item = itemRepository.save(Item.of(request, member));
        List<String> fileNames = imageUtils.uploadMulti(request.getImages(), "test");
        imageService.create(item, fileNames);
        return item;
    }

    /**
     * @description  '/resources/images/test' 경로에 있는 파일을 자동으로 삭제해줌
     */
    private void deleteTestImage () {
        String path = pathResolver.resolve("images", "test").toUri().getPath();
        File folder = new File(path);
        File[] files = folder.listFiles();
        for (File file : files) {
            System.out.println(file.delete());
        }
    }
}

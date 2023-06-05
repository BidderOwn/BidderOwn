//package site.bidderown.server.bounded_context.item.Controller;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
//import site.bidderown.server.bounded_context.image.entity.Image;
//import site.bidderown.server.bounded_context.item.entity.Item;
//import site.bidderown.server.bounded_context.item.repository.ItemRepository;
//import site.bidderown.server.bounded_context.item.service.ItemService;
//import site.bidderown.server.bounded_context.member.entity.Member;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import static net.bytebuddy.agent.builder.AgentBuilder.Default.of;
//import static org.assertj.core.api.Assertions.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//@SpringBootTest
//@AutoConfigureMockMvc
//class ItemControllerTest {
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    ItemService itemService;
//
//    @Autowired
//    ItemRepository itemRepository;
//
//    @Rollback(value = false)
//    @Test
//    @DisplayName("상품 등록")
//    void t01() {
//        Item item = Item.builder()
//                .title("aaa")
//                .description("bbb")
//                .minimumPrice(10000)
//                .build();
//
//        Item result = itemRepository.save(item);
//
//        assertThat(result.getId()).isEqualTo(item.getId());
//    }
//
//    @Rollback(value = false)
//    @Test
//    @Transactional
//    @DisplayName("멤버 연결후 상품 등록 되는지 확인")
//    void t02() {
//        Member member = new Member();
//        member = Member.of("홍길동");
//        ItemRequest itemRequest = new ItemRequest("title", "body", 2000, List.of(new Image()), LocalDateTime.now());
//        Item item = itemService.create(itemRequest, member.getId());
//
//        assertEquals("title", itemRepository.findById(1L).get().getTitle());
//    }
//
//    @Rollback(value = false)
//    @Test
//    @Transactional
//    @DisplayName("전체 상품 가져오기")
//    void t03() {
//        Member member = new Member();
//        member = Member.of("홍길동");
//        ItemRequest itemRequest1 = new ItemRequest("title1", "body1", 2000, List.of(new Image()), LocalDateTime.now());
//        ItemRequest itemRequest2 = new ItemRequest("title2", "body2", 3000, List.of(new Image()), LocalDateTime.now());
//
//        Item itemResponse1 = itemService.create(itemRequest1, member.getId());
//        Item itemResponse2 = itemService.create(itemRequest2, member.getId());
//
//        assertThat(itemRepository.findAll().size()).isEqualTo(2);
//    }
//
//    @Rollback(value = false)
//    @Test
//    @Transactional
//    @DisplayName("id로 한개의 상품 가져오기")
//    void t04() {
//        Member member = new Member();
//        member = Member.of("홍길동");
//        ItemRequest itemRequest1 = new ItemRequest("title1", "body1", 2000, List.of(new Image()), LocalDateTime.now());
//        ItemRequest itemRequest2 = new ItemRequest("title2", "body2", 3000, List.of(new Image()), LocalDateTime.now());
//
//        Item itemResponse1 = itemService.create(itemRequest1, member.getId());
//        Item itemResponse2 = itemService.create(itemRequest2, member.getId());
//
//        assertEquals("title2", itemRepository.findById(2L).get().getTitle());
//    }
//
//    @Rollback(value = false)
//    @Test
//    @Transactional
//    @DisplayName("상품작성자일때 id로 상품 삭제하기")
//    void t05() {
//        Member member = new Member();
//        member = Member.of("홍길동");
//        ItemRequest itemRequest1 = new ItemRequest("title1", "body1", 2000, List.of(new Image()), LocalDateTime.now());
//        ItemRequest itemRequest2 = new ItemRequest("title2", "body2", 3000, List.of(new Image()), LocalDateTime.now());
//
//        Item itemResponse1 = itemService.create(itemRequest1, member.getId());
//        Item itemResponse2 = itemService.create(itemRequest2, member.getId());
//
//        assertEquals("title2", itemRepository.findById(2L).get().getTitle());
//    }
//
//
//
//
//}
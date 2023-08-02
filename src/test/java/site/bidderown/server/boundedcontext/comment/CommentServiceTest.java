package site.bidderown.server.boundedcontext.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.boundedcontext.comment.controller.dto.CommentRequest;
import site.bidderown.server.boundedcontext.comment.entity.Comment;
import site.bidderown.server.boundedcontext.comment.service.CommentService;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemRequest;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.repository.ItemRepository;
import site.bidderown.server.boundedcontext.item.service.ItemService;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.member.service.MemberService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CommentServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void beforeEach() {
        initData();
    }

    @DisplayName("댓글 생성 테스트")
    @Test
    void t001() {
        //given
        Member member = memberService.getMember("test_user_1");
        Item item = itemRepository.findByTitle("test_title_1").get(0);
        String commentText = "test_comment_1";

        //when
        Comment comment = commentService.create(
                CommentRequest.of(commentText),
                item.getId(),
                member.getName()
        );

        //then
        assertThat(comment.getContent()).isEqualTo(commentText);
        assertThat(item.getComments().get(0)).isEqualTo(comment);
    }

    @DisplayName("상품 삭제 후 댓글 생성 테스트")
    @Test
    void t002() {
        //given
        Item item = itemRepository.findByTitle("test_title_1").get(0);
        Member member = memberService.getMember("test_user_1");

        itemService.updateDeleted(item.getId(), member.getName());

        //when
        Throwable exception = assertThrows(
                NotFoundException.class,
                () -> commentService.create(
                        CommentRequest.of("test_comment_1"),
                        item.getId(),
                        member.getName()
                ));

        //then
        assertThat(exception.getMessage().contains("상품")).isTrue();
        assertThat(item.getComments().size()).isEqualTo(0);
    }

    @DisplayName("댓글 삭제 테스트")
    @Test
    void t004() {
        //given
        Member member1 = memberService.getMember("test_user_1");
        Member member2 = memberService.getMember("test_user_2");

        Item item = itemRepository.findByTitle("test_title_1").get(0);

        Comment comment = commentService.create(
                CommentRequest.of("test_comment_2"),
                item.getId(),
                member1.getName()
        );

        //when
        Throwable exception = assertThrows(
                ForbiddenException.class,
                () -> commentService.delete(comment.getId(), member2.getName()));

        //then
        assertThat(exception.getMessage().contains("권한")).isTrue();
    }

    private void initData() {
        Member member1 = memberService.join("test_user_1", "");
        memberService.join("test_user_2", "");
        Item item1 = Item.of(ItemRequest.builder()
                .title("test_title_1")
                .description("test_description_1")
                .minimumPrice(1000)
                .period(3)
                .build(), member1);
        itemRepository.save(item1);
    }
}

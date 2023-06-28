package site.bidderown.server.bounded_context.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.bidderown.server.base.exception.custom_exception.ForbiddenException;
import site.bidderown.server.base.exception.custom_exception.NotFoundException;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentDetailResponse;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.comment.service.CommentService;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.repository.ItemRepository;
import site.bidderown.server.bounded_context.item.service.ItemService;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.member.service.MemberService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

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
                member
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
                        member
                ));

        //then
        assertThat(exception.getMessage().contains("상품")).isTrue();
        assertThat(item.getComments().size()).isEqualTo(0);
    }

    @DisplayName("상품 댓글 전체 개수 테스트")
    @Test
    void t003() {
        //given
        Item item = itemRepository.findByTitle("test_title_1").get(0);
        Member member = memberService.getMember("test_user_1");
        String commentText = "test_comment_";
        int commentCount = 5;

        //when
        for (int i = 0; i < commentCount; i++) {
            commentService.create(
                    CommentRequest.of(commentText + i),
                    item.getId(),
                    member
            );
        }

        List<CommentDetailResponse> comments = commentService.getComments(item.getId());

        //then
        // 댓글 개수
        assertThat(comments.size()).isEqualTo(commentCount);

        // 댓글 내용
        IntStream.range(0, commentCount)
                .forEach(i ->
                        assertThat(item.getComments().get(i).getContent())
                        .isEqualTo(commentText + i));

        // 댓글 정렬 - 최신순
        assertThat(comments).isSortedAccordingTo(
                Comparator.comparing(CommentDetailResponse::getCommentId, Comparator.reverseOrder())
        );
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
                member1
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

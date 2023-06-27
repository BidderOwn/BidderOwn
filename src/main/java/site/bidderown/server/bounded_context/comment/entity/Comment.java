package site.bidderown.server.bounded_context.comment.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member writer;

    @Builder
    private Comment(
            String content,
            Item item,
            Member writer
    ) {
        this.content = content;
        this.item = item;
        this.writer = writer;
    }

    public static Comment of (
        CommentRequest request,
        Item item,
        Member writer
    ) {
        Comment comment = Comment.builder()
                .content(request.getContent())
                .item(item)
                .writer(writer)
                .build();
        item.getComments().add(comment);
        return comment;
    }

    public void updateContent(String content){
        this.content = content;
    }

}

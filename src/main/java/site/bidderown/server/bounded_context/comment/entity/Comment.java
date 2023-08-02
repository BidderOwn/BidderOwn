package site.bidderown.server.bounded_context.comment.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.comment.controller.dto.CommentRequest;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Comment extends BaseEntity {

    @Column(length = 500)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member writer;

    public static Comment of(String content, Item item, Member writer) {
        Comment comment = Comment.builder()
                .content(content)
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

package site.bidderown.server.boundedcontext.comment.entity;

import lombok.*;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

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

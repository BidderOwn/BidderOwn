package site.bidderown.server.bounded_context.item.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {
    //경매 기간 3일 (임시)
    public static final int PERIOD_OF_BIDDING = 3;

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    private int minimumPrice;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    private LocalDateTime expireAt;

    @OneToMany(mappedBy = "item")
    private List<Image> images;

    @Builder
    public Item(
            String title,
            String description,
            int minimumPrice,
            Member member,
            List<Image> images
    ) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.member = member;
        this.images = images;
        this.expireAt = LocalDateTime.now().plusDays(PERIOD_OF_BIDDING);
    }


    public static Item of(ItemRequest request, Member member) {
        return Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .minimumPrice(request.getMinimumPrice())
                .member(member)
                .images(request.getImages())
                .build();
    }
}



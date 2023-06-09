package site.bidderown.server.bounded_context.item.entity;

import lombok.*;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdateDto;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseEntity {

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    private int minimumPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(nullable = false)
    private Member member;

    //private int period;

    private LocalDateTime expireAt;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    // 높은 가격순으로 정렬
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    @OrderBy(value = "price desc")
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    @Builder
    private Item(
            String title,
            String description,
            int minimumPrice,
            Member member,
            LocalDateTime expireAt
    ) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.member = member;
        this.expireAt = expireAt;
        this.itemStatus = ItemStatus.BIDDING;
    }

    public static Item of(ItemRequest request, Member member) {
        return Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .minimumPrice(request.getMinimumPrice())
                .member(member)
                .expireAt(LocalDateTime.now().plusDays(request.getPeriod()))
                .build();
    }

    public void update(ItemUpdateDto itemUpdateDto){

        this.title = itemUpdateDto.getTitle();
        this.description = itemUpdateDto.getDescription();
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void updateStatus(ItemStatus status) {
        this.itemStatus = status;
    }

    // 낙찰 받은 사람
    private Member getWinner() {
        if(bids.isEmpty()) return null;
        Bid bid = bids.get(0); //최고가
        return bid.getBidder();
    }

    // 썸네일 이미지 얻기 (첫번째 사진)
    public String getThumbnailImage() {
        return images.get(0).getFileName();
    }

}



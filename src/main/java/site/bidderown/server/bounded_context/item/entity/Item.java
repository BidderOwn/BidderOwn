package site.bidderown.server.bounded_context.item.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.chat_room.entity.ChatRoom;
import site.bidderown.server.bounded_context.comment.entity.Comment;
import site.bidderown.server.bounded_context.heart.entity.Heart;
import site.bidderown.server.bounded_context.image.entity.Image;
import site.bidderown.server.bounded_context.item.controller.dto.ItemRequest;
import site.bidderown.server.bounded_context.item.controller.dto.ItemUpdate;
import site.bidderown.server.bounded_context.item.entitylistener.ItemEntityListener;
import site.bidderown.server.bounded_context.member.entity.Member;
import site.bidderown.server.bounded_context.notification.entity.Notification;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(value = ItemEntityListener.class)
public class Item extends BaseEntity {

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String description;

    private int minimumPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    private LocalDateTime expireAt;

    @ColumnDefault("false")
    private boolean deleted;

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<>();

    // 높은 가격순으로 정렬
    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    @OrderBy(value = "price desc")
    private List<Bid> bids = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.REMOVE)
    private List<Heart> hearts = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ItemStatus itemStatus;

    private String thumbnailImageFileName;

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
        LocalDateTime expireAt = TimeUtils.setExpireAt(request.getPeriod());
        return Item.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .minimumPrice(request.getMinimumPrice())
                .member(member)
                .expireAt(expireAt)
                .build();
    }

    public void update(ItemUpdate itemUpdate) {

        this.title = itemUpdate.getTitle();
        this.description = itemUpdate.getDescription();
        this.setUpdatedAt(LocalDateTime.now());
    }

    public void updateStatus(ItemStatus status) {
        this.itemStatus = status;
    }

    public void soldOutItem() {
        this.itemStatus = ItemStatus.SOLDOUT;
    }

    public void closeBid() {
        this.itemStatus = ItemStatus.BID_END;
    }

    // 낙찰 받은 사람
    private Member getWinner() {
        if (bids.isEmpty()) return null;
        Bid bid = bids.get(0); //최고가
        return bid.getBidder();
    }

    // 썸네일 이미지 얻기 (첫번째 사진)
    public String getThumbnailImage() {
        if (images.isEmpty()) return null;
        return images.get(0).getFileName();
    }

    public void updateDeleted() {
        this.deleted = true;
    }

    public void setThumbnailImageFileName(String thumbnailImageFileName) {
        this.thumbnailImageFileName = thumbnailImageFileName;
    }
}



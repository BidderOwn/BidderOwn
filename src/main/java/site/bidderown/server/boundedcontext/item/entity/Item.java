package site.bidderown.server.boundedcontext.item.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.chatroom.entity.ChatRoom;
import site.bidderown.server.boundedcontext.comment.entity.Comment;
import site.bidderown.server.boundedcontext.heart.entity.Heart;
import site.bidderown.server.boundedcontext.image.entity.Image;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemRequest;
import site.bidderown.server.boundedcontext.item.entitylistener.ItemEntityListener;
import site.bidderown.server.boundedcontext.item.controller.dto.ItemUpdateRequest;
import site.bidderown.server.boundedcontext.member.entity.Member;
import site.bidderown.server.boundedcontext.notification.entity.Notification;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(indexes = @Index(
        name = "idx_item_expire_at",
        columnList = "expireAt, itemStatus, deleted")
)
@EntityListeners(value = ItemEntityListener.class)
public class Item extends BaseEntity {

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    private int minimumPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
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

    public void update(ItemUpdateRequest itemUpdateRequest) {
        this.title = itemUpdateRequest.getTitle();
        this.description = itemUpdateRequest.getDescription();
    }

    public void soldOutItem() {
        this.itemStatus = ItemStatus.SOLDOUT;
    }

    public void closeBid() {
        this.itemStatus = ItemStatus.BID_END;
    }

    public void updateDeleted() {
        this.deleted = true;
    }

    public void setThumbnailImageFileName(String thumbnailImageFileName) {
        this.thumbnailImageFileName = thumbnailImageFileName;
    }
}



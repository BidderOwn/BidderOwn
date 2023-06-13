package site.bidderown.server.bounded_context.notification.entity;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    private LocalDateTime readDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;


    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Builder
    public Notification(Item item, Member receiver, NotificationType notificationType) {
        this.item = item;
        this.receiver = receiver;
        this.notificationType = notificationType;
    }

    public static Notification of(Item item, Member receiver, NotificationType notificationType) {
        return Notification.builder()
                .item(item)
                .receiver(receiver)
                .notificationType(notificationType)
                .build();
    }

    public void read() {
        this.readDate = LocalDateTime.now();
    }
}
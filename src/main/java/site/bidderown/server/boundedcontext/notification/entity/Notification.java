package site.bidderown.server.boundedcontext.notification.entity;


import lombok.*;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class Notification extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member receiver;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private LocalDateTime readDate;

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
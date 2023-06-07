package site.bidderown.server.bounded_context.notification.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.base.base_entity.BaseEntity;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;
import site.bidderown.server.bounded_context.member.entity.Member;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

    private LocalDateTime readDate;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne
    private Member receiver;

    @ManyToOne
    private Item item;

}
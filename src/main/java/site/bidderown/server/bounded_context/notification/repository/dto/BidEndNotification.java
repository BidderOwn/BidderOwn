package site.bidderown.server.bounded_context.notification.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter
@Setter
@AllArgsConstructor
public class BidEndNotification {
    private Item item;
    private Member receiver;
}
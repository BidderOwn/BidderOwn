package site.bidderown.server.boundedcontext.notification.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.member.entity.Member;

@Getter
@Setter
@AllArgsConstructor
public class BidEndNotification {
    private Item item;
    private Member receiver;
}
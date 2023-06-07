package site.bidderown.server.base.event;

import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.member.entity.Member;

public class EventItemNotification {

    private Item item;
    private Member receiver;
    private NotificationType type;
}

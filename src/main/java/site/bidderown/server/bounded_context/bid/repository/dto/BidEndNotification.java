package site.bidderown.server.bounded_context.bid.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BidEndNotification {
    private Long itemId;
    private Long receiverId;
}

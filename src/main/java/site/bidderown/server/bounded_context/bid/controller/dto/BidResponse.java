package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.bid.entity.Bid;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidResponse {
    private String bidderName;
    private int bidPrice; // 입찰 금액
    private Long itemId;
    private String createdDate;
    // img도 들어가야할 것 같습니다.


    @Builder
    private BidResponse(String bidderName, String createdDate, int bidPrice, Long itemId) {
        this.bidderName = bidderName;
        this.createdDate = createdDate;
        this.bidPrice = bidPrice;
        this.itemId = itemId;
    }

    public static BidResponse of(Bid bid, Item item){
        Duration duration = Duration.between(bid.getCreatedAt(), LocalDateTime.now());
        String createdAt;

        if(duration.toMinutes() < 1)
        {
            createdAt = "방금 전";
        } else if (duration.toMinutes() < 60) {
            createdAt = duration.toMinutes() + "분 전";
        } else if (duration.toMinutes() < 1440) {
            createdAt = duration.toHours() + "시간 전";
        }
        else
            createdAt = duration.toDays() + "일 전";


        return BidResponse.builder()
                .bidderName(bid.getBidder().getName())
                .bidPrice(bid.getPrice())
                .createdDate(createdAt)
                .itemId(item.getId()).build();
    }
}

package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;
import site.bidderown.server.base.util.TimeUtils;
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
    private String createdDate;
    private String thumbnailImageName;
    // img도 들어가야할 것 같습니다.


    @Builder
    private BidResponse(String bidderName, String createdDate, int bidPrice, String thumbnailImageName) {
        this.bidderName = bidderName;
        this.createdDate = createdDate;
        this.bidPrice = bidPrice;
        this.thumbnailImageName = thumbnailImageName;
    }

    public static BidResponse of(Bid bid, Item item){

        String createdAt = TimeUtils.getRegistrationTime(item.getCreatedAt(), LocalDateTime.now());

        return BidResponse.builder()
                .bidderName(bid.getBidder().getName())
                .bidPrice(bid.getPrice())
                .createdDate(createdAt)
                .thumbnailImageName(item.getThumbnailImage())
                .build();
    }
}

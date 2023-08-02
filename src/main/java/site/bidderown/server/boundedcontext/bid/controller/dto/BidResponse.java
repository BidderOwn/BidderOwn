package site.bidderown.server.boundedcontext.bid.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.boundedcontext.bid.entity.Bid;
import site.bidderown.server.boundedcontext.item.entity.Item;

import java.time.LocalDateTime;

@Schema(description = "입찰 응답")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidResponse {
    @Schema(description = "입찰자 이름")
    private String bidderName;
    @Schema(description = "입찰금액")
    private int bidPrice;
    @Schema(description = "생성일자")
    private String createdDate;
    @Schema(description = "썸네일사진 이름")
    private String thumbnailImageName;
    @Schema(description = "입찰ID")
    private Long bidId;


    @Builder
    private BidResponse(String bidderName, String createdDate, int bidPrice, String thumbnailImageName, Long bidId) {
        this.bidderName = bidderName;
        this.createdDate = createdDate;
        this.bidPrice = bidPrice;
        this.thumbnailImageName = thumbnailImageName;
        this.bidId = bidId;
    }

    public static BidResponse of(Bid bid, Item item){

        String createdAt = TimeUtils.getRegistrationTime(item.getCreatedAt(), LocalDateTime.now());

        return BidResponse.builder()
                .bidderName(bid.getBidder().getName())
                .bidPrice(bid.getPrice())
                .createdDate(createdAt)
                .thumbnailImageName(item.getThumbnailImageFileName())
                .bidId(bid.getId())
                .build();
    }
}

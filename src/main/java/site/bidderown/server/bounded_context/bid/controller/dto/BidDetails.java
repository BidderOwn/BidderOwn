package site.bidderown.server.bounded_context.bid.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.entity.Item;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.time.Duration;
import java.time.LocalDateTime;


@Schema(description = "입찰 상세")
@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidDetails {
    @Schema(description = "희망 가격")
    private int desiredPrice;
    @Schema(description = "입찰 최고가")
    private Integer maxPrice;
    @Schema(description = "입찰 최저가")
    private Integer minPrice;
    @Schema(description = "입찰 평균가")
    private Integer avgPrice;
    @Schema(description = "상품 제목")
    private String itemTitle;
    @Schema(description = "판매자 이름")
    private String sellerName;
    @Schema(description = "썸네일 사진 이름")
    private String imgName;
    @Schema(description = "경매 종료 일자")
    private String expireAt;
    @Schema(description = "상품 상태", allowableValues = {"BIDDING", "SOLDOUT", "BID_END"})
    private ItemStatus itemStatus;

    @Builder
    public BidDetails(String imgName, String sellerName, int desiredPrice, Integer maxPrice, Integer minPrice, Integer avgPrice, String itemTitle, String expireAt, ItemStatus itemStatus) {
        this.desiredPrice = desiredPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.avgPrice = avgPrice;
        this.itemTitle = itemTitle;
        this.sellerName = sellerName;
        this.imgName = imgName;
        this.expireAt = expireAt;
        this.itemStatus = itemStatus;
    }

    public static BidDetails of(Item item, Integer maxPrice, Integer minPrice, Integer avgPrice) {
        String image = "";
        if (item.getImages().size() > 0) {
            image = item.getImages().get(0).getFileName();
        }
        String expireAt = TimeUtils.getRemainingTime(LocalDateTime.now(), item.getExpireAt());


        return BidDetails.builder()
                .sellerName(item.getMember().getName())
                .desiredPrice(item.getMinimumPrice())
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .avgPrice(avgPrice)
                .itemTitle(item.getTitle())
                .imgName(image)
                .expireAt(expireAt)
                .itemStatus(item.getItemStatus())
                .build();
    }

}

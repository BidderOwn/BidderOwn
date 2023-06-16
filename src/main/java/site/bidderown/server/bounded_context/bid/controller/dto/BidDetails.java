package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;
import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.Duration;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BidDetails {
    private int desiredPrice;
    private Integer maxPrice;
    private Integer minPrice;
    private Integer avgPrice;
    private String itemTitle;
    private String sellerName;
    private String imgName;
    private String expireAt;
    private String itemStatus;

    @Builder
    public BidDetails(String imgName, String sellerName, int desiredPrice, Integer maxPrice, Integer minPrice, Integer avgPrice, String itemTitle, String expireAt, String itemStatus) {
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
                .itemStatus(item.getItemStatus().getStatus())
                .build();
    }

}

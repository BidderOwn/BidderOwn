package site.bidderown.server.bounded_context.bid.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.item.entity.Item;

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

    @Builder
    public BidDetails(String imgName, String sellerName, int desiredPrice, Integer maxPrice, Integer minPrice, Integer avgPrice, String itemTitle) {
        this.desiredPrice = desiredPrice;
        this.maxPrice = maxPrice;
        this.minPrice = minPrice;
        this.avgPrice = avgPrice;
        this.itemTitle = itemTitle;
        this.sellerName = sellerName;
        this.imgName = imgName;
    }

    public static BidDetails of(Item item, Integer maxPrice, Integer minPrice, Integer avgPrice) {
        String image = "";
        if (item.getImages().size() > 0) {
            image = item.getImages().get(0).getFileName();
        }
        return BidDetails.builder()
                .sellerName(item.getMember().getName())
                .desiredPrice(item.getMinimumPrice())
                .maxPrice(maxPrice)
                .minPrice(minPrice)
                .avgPrice(avgPrice)
                .itemTitle(item.getTitle())
                .imgName(image)
                .build();
    }

}

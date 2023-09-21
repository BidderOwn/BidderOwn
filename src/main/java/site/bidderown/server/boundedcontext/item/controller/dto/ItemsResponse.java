package site.bidderown.server.boundedcontext.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.boundedcontext.item.entity.Item;
import site.bidderown.server.boundedcontext.item.entity.ItemStatus;

import java.time.LocalDateTime;

@Schema(description = "상품 응답")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemsResponse {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "제목")
    private String title;
    @Schema(description = "최소희망가격")
    private int minimumPrice;
    @Schema(description = "입찰수")
    private Integer bidCount;
    @Schema(description = "댓글수")
    private Integer commentsCount;
    @Schema(description = "좋아요수")
    private Integer heartsCount;
    @Schema(description = "썸네일사진 이름")
    private String thumbnailImageName;
    @Schema(description = "상품 상태", allowableValues = {"BIDDING","SOLDOUT","BID_END"})
    private ItemStatus itemStatus;
    @Schema(description = "만료 일자")
    private LocalDateTime expireAt;

    @Builder
    public ItemsResponse(
            Long id,
            String title,
            int minimumPrice,
            Integer bidCount,
            Integer commentsCount,
            Integer heartsCount,
            String thumbnailImageName,
            ItemStatus itemStatus,
            LocalDateTime expireAt
    ) {
        this.id = id;
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.commentsCount = commentsCount;
        this.bidCount = bidCount;
        this.heartsCount = heartsCount;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
        this.expireAt = expireAt;
    }

    public static ItemsResponse of__v12(Item item) {
        return ItemsResponse.builder()
                .id(item.getId())
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .bidCount(item.getBids().size())
                .commentsCount(item.getComments().size())
                .heartsCount(item.getHearts().size())
                .thumbnailImageName(item.getThumbnailImageFileName())
                .itemStatus(item.getItemStatus())
                .expireAt(item.getExpireAt())
                .build();
    }
}

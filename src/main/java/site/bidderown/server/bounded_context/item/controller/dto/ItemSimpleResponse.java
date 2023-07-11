package site.bidderown.server.bounded_context.item.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import site.bidderown.server.base.util.TimeUtils;
import site.bidderown.server.bounded_context.item.entity.Item;

import java.time.LocalDateTime;

@Schema(description = "상품단순 요청")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemSimpleResponse {
    @Schema(description = "제목")
    private String title;
    @Schema(description = "최소희망가격")
    private int minimumPrice;
    @Schema(description = "만료 일자")
    private String expireAt;
    @Schema(description = "ID")
    private Long itemId;
    @Schema(description = "썸네일 사진 이름")
    private String thumbnailImageName;
    @Schema(description = "상품 상태", allowableValues = {"BIDDING","SOLDOUT","BID_END"})
    private String itemStatus;

    @Builder
    public ItemSimpleResponse(String title, int minimumPrice, String expireAt, Long itemId, String thumbnailImageName, String itemStatus) {
        this.title = title;
        this.minimumPrice = minimumPrice;
        this.expireAt = expireAt;
        this.itemId = itemId;
        this.thumbnailImageName = thumbnailImageName;
        this.itemStatus = itemStatus;
    }

    public static ItemSimpleResponse of(Item item) {
        String expireAt = TimeUtils.getRemainingTime(LocalDateTime.now(), item.getExpireAt());

        return ItemSimpleResponse.builder()
                .title(item.getTitle())
                .minimumPrice(item.getMinimumPrice())
                .expireAt(expireAt)
                .itemId(item.getId())
                .thumbnailImageName(item.getThumbnailImage())
                .itemStatus(item.getItemStatus().getStatus())
                .build();
    }
}


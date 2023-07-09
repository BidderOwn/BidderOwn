package site.bidderown.server.bounded_context.item.repository.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.item.entity.ItemStatus;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BulkInsertItem {

    private String title;
    private String description;
    private int minimumPrice;
    private Long memberId;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private LocalDateTime expiredDate;
    private String itemStatus;
    @Builder
    public BulkInsertItem(String title, String description, int minimumPrice, Long memberId) {
        this.title = title;
        this.description = description;
        this.minimumPrice = minimumPrice;
        this.memberId = memberId;
        this.createdDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
        this.expiredDate = LocalDateTime.now().plusDays(3);
        this.itemStatus = String.valueOf(ItemStatus.BIDDING);
    }
}
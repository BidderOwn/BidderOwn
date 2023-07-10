package site.bidderown.server.bounded_context.item.repository.dto;

import lombok.*;

/**
 * redis 에 item count 정보를 더 저장하게 되면 이 DTO 에 데이터를 추가하면 됩니다.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemCounts {
    private int bidCount;
    private int commentCount;
    private int heartCount;

    public static ItemCounts newInstance() {
        return ItemCounts.builder()
                .bidCount(0)
                .commentCount(0)
                .heartCount(0)
                .build();
    }

    public static ItemCounts of(int bidCount, int commentCount, int heartCount) {
        return ItemCounts.builder()
                .bidCount(bidCount)
                .commentCount(commentCount)
                .heartCount(heartCount)
                .build();
    }
}

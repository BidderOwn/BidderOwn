package site.bidderown.server.bounded_context.heart.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.heart.entity.Heart;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartResponse {

    private Long itemId;
    private Long memberId;
    private Boolean likeStatus;

    @Builder
    public HeartResponse (
            Long itemId,
            Long memberId,
            Boolean likeStatus
    ) {
        this.itemId = itemId;
        this.memberId = memberId;
        this.likeStatus = likeStatus;
    }

    public static HeartResponse of (Heart heart, Boolean likeStatus) {
        return HeartResponse.builder()
                .itemId(heart.getItem().getId())
                .memberId(heart.getMember().getId())
                .likeStatus(likeStatus)
                .build();
    }
}

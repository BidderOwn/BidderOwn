package site.bidderown.server.bounded_context.heart.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import site.bidderown.server.bounded_context.heart.entity.Heart;

@Schema(description = "좋아요 응답")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class HeartResponse {

    @Schema(description = "상품ID")
    private Long itemId;
    @Schema(description = "멤버ID")
    private Long memberId;
    @Schema(description = "좋아요 여부")
    private Boolean likeStatus;

    public static HeartResponse of(Heart heart, Boolean likeStatus) {
        return HeartResponse.builder()
                .itemId(heart.getItem().getId())
                .memberId(heart.getMember().getId())
                .likeStatus(likeStatus)
                .build();
    }
}

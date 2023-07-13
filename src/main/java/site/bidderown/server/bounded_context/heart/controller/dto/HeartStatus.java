package site.bidderown.server.bounded_context.heart.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "좋아요상태 DTO")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartStatus {
    @Schema(description = "좋아요상태")
    private Boolean likeStatus;

    @Builder
    public HeartStatus (Boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public static HeartStatus of (Boolean likeStatus) {
        return HeartStatus.builder().likeStatus(likeStatus).build();
    }
}

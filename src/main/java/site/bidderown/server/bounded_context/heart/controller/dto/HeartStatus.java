package site.bidderown.server.bounded_context.heart.controller.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HeartStatus {
    private Boolean likeStatus;

    @Builder
    public HeartStatus (Boolean likeStatus) {
        this.likeStatus = likeStatus;
    }

    public static HeartStatus of (Boolean likeStatus) {
        return HeartStatus.builder().likeStatus(likeStatus).build();
    }
}

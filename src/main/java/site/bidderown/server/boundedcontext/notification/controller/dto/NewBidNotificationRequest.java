package site.bidderown.server.boundedcontext.notification.controller.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewBidNotificationRequest {
    private Long itemId;
    private String memberName;

    @Builder
    private NewBidNotificationRequest(Long itemId, String memberName) {
        this.itemId = itemId;
        this.memberName = memberName;
    }
    public static NewBidNotificationRequest of(Long itemId, String memberName){
        return NewBidNotificationRequest.builder()
                .itemId(itemId)
                .memberName(memberName)
                .build();
    }

}

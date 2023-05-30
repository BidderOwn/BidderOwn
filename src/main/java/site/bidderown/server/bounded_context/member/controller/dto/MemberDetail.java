package site.bidderown.server.bounded_context.member.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter @Setter
@AllArgsConstructor
@Builder
public class MemberDetail {
    private String name;

    public static MemberDetail from(Member member) {
        return MemberDetail.builder()
                .name(member.getName())
                .build();
    }
}

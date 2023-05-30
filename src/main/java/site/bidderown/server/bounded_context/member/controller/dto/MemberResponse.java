package site.bidderown.server.bounded_context.member.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter @Setter
@AllArgsConstructor
@Builder
public class MemberResponse {
    private String name;

    public static MemberResponse from(Member member) {
        return MemberResponse.builder()
                .name(member.getName())
                .build();
    }
}

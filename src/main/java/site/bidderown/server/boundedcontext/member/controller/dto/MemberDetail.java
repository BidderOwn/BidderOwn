package site.bidderown.server.boundedcontext.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import site.bidderown.server.boundedcontext.member.entity.Member;

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

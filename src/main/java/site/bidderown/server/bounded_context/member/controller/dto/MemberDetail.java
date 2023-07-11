package site.bidderown.server.bounded_context.member.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import site.bidderown.server.bounded_context.member.entity.Member;

@Getter @Setter
@AllArgsConstructor
@Builder
public class MemberDetail {
    private String name;

    public static MemberDetail of(Member member) {
        return MemberDetail.builder()
                .name(member.getName())
                .build();
    }
}

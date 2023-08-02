package site.bidderown.server.boundedcontext.member.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import site.bidderown.server.base.baseentity.BaseEntity;
import site.bidderown.server.boundedcontext.bid.entity.Bid;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {
    @Column(unique = true)
    private String name;

    private String password;

    @OneToMany(mappedBy = "bidder")
    private List<Bid> bids = new ArrayList<>();

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        return List.of(new SimpleGrantedAuthority("member"));
    }

    @Builder
    private Member(String name, String password) {
        this.password = password;
        this.name = name;
    }

    public static Member from(String name) {
        return Member.builder()
                .name(name)
                .build();
    }

    public static Member of(String name, String password) {
        return Member.builder()
                .name(name)
                .password(password)
                .build();
    }
}


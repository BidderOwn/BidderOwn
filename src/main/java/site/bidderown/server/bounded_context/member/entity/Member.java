package site.bidderown.server.bounded_context.member.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import site.bidderown.server.base.base_entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@ToString(callSuper = true)
@Entity
public class Member extends BaseEntity {
    @Column(unique = true)
    private String name;

    @Builder
    private Member(String name) {
        this.name = name;
    }

    public static Member of(String name) {
        return Member.builder()
                .name(name)
                .build();
    }
}

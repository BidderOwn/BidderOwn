package site.bidderown.server.bounded_context.users.entity;

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
@SuperBuilder
@Entity
public class Users extends BaseEntity {
    @Column(unique = true)
    private String username;

    public static Users of(String username) {
        return Users.builder()
                .username(username)
                .build();
    }
}

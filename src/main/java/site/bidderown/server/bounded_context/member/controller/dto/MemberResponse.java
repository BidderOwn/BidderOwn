package site.bidderown.server.bounded_context.users.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.users.entity.Users;

@Getter @Setter
@AllArgsConstructor
@Builder
public class UsersResponse {
    private String username;

    public static UsersResponse from(Users user) {
        return UsersResponse.builder()
                .username(user.getUsername())
                .build();
    }

}

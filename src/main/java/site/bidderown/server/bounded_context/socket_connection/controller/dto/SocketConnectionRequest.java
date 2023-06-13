package site.bidderown.server.bounded_context.socket_connection.controller.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;

@Getter
@Setter
@NoArgsConstructor
public class SocketConnectionRequest {
    private Long connectionId;
    private String connectionType;
}

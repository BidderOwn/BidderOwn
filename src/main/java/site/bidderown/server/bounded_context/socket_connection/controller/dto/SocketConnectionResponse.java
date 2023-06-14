package site.bidderown.server.bounded_context.socket_connection.controller.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketConnectionResponse {

    private Long connectionId;
    private String connectionUrl;

    @Builder
    public SocketConnectionResponse(SocketConnection socketConnection) {
        this.connectionId = socketConnection.getConnectionId();
        this.connectionUrl = socketConnection.getConnectionType().getSocketPath();
    }

    public static SocketConnectionResponse of(SocketConnection socketConnection) {
        return SocketConnectionResponse.builder()
                .socketConnection(socketConnection)
                .build();
    }
}

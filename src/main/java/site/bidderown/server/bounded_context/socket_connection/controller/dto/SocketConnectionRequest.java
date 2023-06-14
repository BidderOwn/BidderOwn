package site.bidderown.server.bounded_context.socket_connection.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketConnectionRequest {
    private Long connectionId;
    private ConnectionType connectionType;

    @Builder
    public SocketConnectionRequest(Long connectionId, ConnectionType connectionType) {
        this.connectionId = connectionId;
        this.connectionType = connectionType;
    }

    public static SocketConnectionRequest of(Long connectionId, ConnectionType connectionType) {
        return SocketConnectionRequest.builder()
                .connectionId(connectionId)
                .connectionType(connectionType)
                .build();
    }
}

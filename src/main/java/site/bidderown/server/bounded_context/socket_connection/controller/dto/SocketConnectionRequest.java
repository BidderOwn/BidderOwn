package site.bidderown.server.bounded_context.socket_connection.controller.dto;

import lombok.*;
import site.bidderown.server.bounded_context.socket_connection.entity.SocketConnection;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketConnectionRequest {
    private Long connectionId;
    private String connectionType;

    @Builder
    public SocketConnectionRequest(Long connectionId, String connectionType) {
        this.connectionId = connectionId;
        this.connectionType = connectionType;
    }

    public static SocketConnectionRequest of(Long connectionId, String connectionType) {
        return SocketConnectionRequest.builder()
                .connectionId(connectionId)
                .connectionType(connectionType)
                .build();
    }
}

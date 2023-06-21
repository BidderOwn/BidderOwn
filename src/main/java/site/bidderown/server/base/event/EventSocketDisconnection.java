package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

@Getter
@NoArgsConstructor
public class EventSocketDisconnection {
    private Long connectionId;
    private ConnectionType connectionType;

    @Builder
    public EventSocketDisconnection(Long connectionId, ConnectionType connectionType) {
        this.connectionId = connectionId;
        this.connectionType = connectionType;
    }

    public static EventSocketDisconnection of(Long connectionId, ConnectionType connectionType) {
        return EventSocketDisconnection.builder()
                .connectionId(connectionId)
                .connectionType(connectionType)
                .build();
    }
}

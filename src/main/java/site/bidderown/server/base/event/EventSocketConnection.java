package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.socket_connection.entity.ConnectionType;

@Getter
@NoArgsConstructor
public class EventSocketConnection {
    private String memberName;
    private Long connectionId;
    private ConnectionType connectionType;

    @Builder
    public EventSocketConnection(String memberName, Long connectionId, ConnectionType connectionType) {
        this.memberName = memberName;
        this.connectionId = connectionId;
        this.connectionType = connectionType;
    }

    public static EventSocketConnection of(String memberName, Long connectionId, ConnectionType connectionType) {
        return EventSocketConnection.builder()
                .memberName(memberName)
                .connectionId(connectionId)
                .connectionType(connectionType)
                .build();
    }
}

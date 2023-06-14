package site.bidderown.server.base.event;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionRequest;

@Getter
@NoArgsConstructor
public class EventSocketConnection {
    String memberName;
    SocketConnectionRequest socketConnectionRequest;

    @Builder
    public EventSocketConnection(String memberName, SocketConnectionRequest socketConnectionRequest) {
        this.memberName = memberName;
        this.socketConnectionRequest = socketConnectionRequest;
    }

    public static EventSocketConnection of(String memberName, SocketConnectionRequest socketConnectionRequest) {
        return EventSocketConnection.builder()
                .memberName(memberName)
                .socketConnectionRequest(socketConnectionRequest)
                .build();
    }
}

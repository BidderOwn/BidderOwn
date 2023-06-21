package site.bidderown.server.bounded_context.socket_connection.event_handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import site.bidderown.server.base.event.EventSocketConnection;
import site.bidderown.server.base.event.EventSocketDisconnection;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionRequest;
import site.bidderown.server.bounded_context.socket_connection.service.SocketConnectionService;

@Slf4j
@Component
@RequiredArgsConstructor
public class SocketConnectionEventHandler {

    private final SocketConnectionService socketConnectionService;

    @EventListener
    @Async
    public void listen(EventSocketConnection eventSocketConnection) {
        socketConnectionService.create(
                eventSocketConnection.getMemberName(),
                SocketConnectionRequest.of(
                        eventSocketConnection.getConnectionId(),
                        eventSocketConnection.getConnectionType()
                )
        );
    }

    @EventListener
    @Async
    public void listen(EventSocketDisconnection eventSocketDisconnection) {
        socketConnectionService.disconnect(
                eventSocketDisconnection.getConnectionId(),
                eventSocketDisconnection.getConnectionType()
        );
    }
}

package site.bidderown.server.bounded_context.socket_connection.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionRequest;
import site.bidderown.server.bounded_context.socket_connection.controller.dto.SocketConnectionResponse;
import site.bidderown.server.bounded_context.socket_connection.service.SocketConnectionService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/connection")
@RestController
public class SocketConnectionController {

    private final SocketConnectionService socketConnectionService;

    @GetMapping("/me")
    public List<SocketConnectionResponse> connectionResponses(@AuthenticationPrincipal User user) {
        return socketConnectionService.getConnections(user.getUsername());
    }

    @PostMapping
    public Long connectionCreate(
            @RequestBody SocketConnectionRequest request,
            @AuthenticationPrincipal User user
    ) {
        return socketConnectionService.create(user.getUsername(), request);
    }
}
